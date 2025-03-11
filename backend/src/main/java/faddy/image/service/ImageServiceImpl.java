package faddy.image.service;

import faddy.global.exception.BadRequestException;
import faddy.global.exception.ExceptionCode;
import faddy.global.exception.ImageException;
import faddy.image.domain.Image;
import faddy.image.dto.ImageFile;
import faddy.image.dto.ImageRequestDto;
import faddy.image.dto.ImageResponseDto;
import faddy.image.dto.request.ImageLookupRequestDTO;
import faddy.image.infrastructure.ImageMapper;
import faddy.image.repository.ImageRepository;
import faddy.image.type.ImageCategory;
import faddy.log.exception.ExceptionLogger;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static faddy.global.exception.ExceptionCode.NULL_IMAGE;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageServiceImpl implements ImageService {
    private static final String EXTENSION_DELIMITER = ".";
    private static final String DIRECTORY_DELIMITER = "/";

    private final ImageUploader s3ImageUploader;
    private final ImageRepository imageRepository;


    public ImageResponseDto uploadImage(final MultipartFile file, ImageCategory category) throws IOException {
        // 이미지 유효성 검사
        validateNullImage(file);

        // image file s3 버킷에 업로드
        String uploadImageUrl = s3ImageUploader.upload(file, category);

        ImageResponseDto imageResponseDto;
        try {
            imageResponseDto = ImageResponseDto.fromImageFile(new ImageFile(file, uploadImageUrl, category));
        } catch (Exception e) {
            throw new ImageException(ExceptionCode.FAIL_IMAGE_UPLOADING);
        }

        // 이미지 저장
        try {
            saveImage(imageResponseDto);
        } catch (Exception e) {
            // s3 버킷에 저장된 이미지 객체 삭제
            String filename = s3ImageUploader.extractObjectKeyFromObjUrl(imageResponseDto.getUrl());
            log.info(filename);
            s3ImageUploader.deleteFile(filename);

        }

        return imageResponseDto;
    }

    @Override
    @Transactional
    public void saveImage(final ImageResponseDto dto) {

        final Image image = ImageMapper.uploadImageResponseToEntity(dto);

        try {
            imageRepository.save(image);
            log.info("success!");
        } catch (Exception e) {
            throw new ImageException(ExceptionCode.SAVE_IMAGE_ERROR);
        }
    }

    private void validateNullImage(final MultipartFile file) {
        if (file.isEmpty()) {
            throw new ImageException(NULL_IMAGE);
        }
    }

    @Override
    public void validateImageFile(MultipartFile file) {
        // 파일 크기 검증 (10MB)
        if (file.getSize() > 10_000_000) { // 10MB
            throw new ImageException(ExceptionCode.FILE_SIZE_TOO_LARGE);
        }

        // 파일 형식 검증 (JPEG, PNG)
        String contentType = file.getContentType();
        if (contentType == null ||
                !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
            throw new ImageException(ExceptionCode.INVALID_AUTH_CODE);
        }
    }

    @Override
    @Transactional
    public void deleteImage(String hashName) {
        // 이미지 정보 조회
        Image image = imageRepository.findByHashName(hashName)
                .orElseThrow(() -> new ImageException(ExceptionCode.IMAGE_NOT_FOUND));

        // S3에서 이미지 파일 삭제
        try {
            String filename = s3ImageUploader.extractObjectKeyFromObjUrl(image.getImageUrl());
            s3ImageUploader.deleteFile(filename);
        } catch (Exception e) {
            log.error("Failed to delete image file in S3", e);
            throw new ImageException(ExceptionCode.FAIL_DELETE_IMAGE_FILE);
        }

        // 데이터베이스에서 이미지 정보 삭제
        try {
            imageRepository.delete(image);

            log.info("Deleted image: {}", hashName);
        } catch (Exception e) {
            log.error("Failed to delete image info in database", e);
            throw new ImageException(ExceptionCode.FAIL_DELETE_IMAGE_INFO);
        }
    }

    @Override
    @Transactional
    public void deleteImages(@NotEmpty List<ImageRequestDto> images) {

        List<String> hashedNames = getHashedNames(images);
        List<String> urls = getUrls(images);


        if (hashedNames.isEmpty() || urls.isEmpty()) {
            throw new ImageException(ExceptionCode.IMAGE_NOT_FOUND);
        }
        try {
            // image 엔티티 삭제
            imageRepository.deleteAllByHashNameIn(hashedNames);

        } catch (Exception e) {
            log.info(e.getMessage());
            throw new ImageException(ExceptionCode.FAIL_DELETE_IMAGE_INFO);
        }

        // s3 이미지 데이터 삭제
        try {
            List<String> filenames = getFileNames(urls);
            s3ImageUploader.deleteFiles(filenames);
        } catch (Exception e) {
            log.error("Failed to delete image file in S3", e);
            throw new ImageException(ExceptionCode.FAIL_DELETE_IMAGE_FILE);
        }
    }


    private List<String> getFileNames(List<String> urls) {
        return urls.stream()
                .map(url -> {
                    try {
                        return s3ImageUploader.extractObjectKeyFromObjUrl(url);
                    } catch (Exception e) {
                        log.warn("\"Invalid file URL: \" + hashedName, e");
                        throw new ImageException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid file URL: " + url + e.getMessage());
                    }
                })
                .collect(Collectors.toList());

    }

    private List<String> getHashedNames(List<ImageRequestDto> images) {
        return images.stream()
                .map(ImageRequestDto::getHashedName)
                .collect(Collectors.toList());
    }

    private List<String> getUrls(List<ImageRequestDto> images) {
        return images.stream()
                .map(ImageRequestDto::getUrl)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Image findById(Long imageId) {
        Optional<Image> image = imageRepository.findById(imageId);
        return image.orElse(null);
    }

    public List<Image> findByHashedNames(List<String> hashedNames) {
        // 리스트가 null이거나 비어있는 경우 검사
        if (hashedNames == null || hashedNames.isEmpty()) {
            throw new BadRequestException(400, "이미지 해쉬 이름 리스트가 존재하지 않습니다.");
        }

        // 리스트 요소 중 null이 있는지 검사
        if (hashedNames.stream().anyMatch(Objects::isNull)) {
            throw new BadRequestException(400, "이미지 해쉬 이름 리스트에 null 값이 포함되어 있습니다.");
        }

        // 리스트의 해쉬 이름을 사용하여 이미지 찾기 로직 구현
        return imageRepository.findByHashedNameIn(hashedNames);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Image> findByImageUrl(List<ImageLookupRequestDTO> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            return Collections.emptyList();
        }

        try {
            List<String> urls = imageUrls.stream()
                    .map(ImageLookupRequestDTO::imageUrl)
                    .collect(Collectors.toList());

            return imageRepository.findByImageUrlIn(urls);

        } catch (Exception e) {
            ExceptionLogger.logException(e);
            throw new ImageException(HttpStatus.BAD_REQUEST.value() , "이미지 URL 조회 실패" , e);
        }
    }


    @Override
    @Transactional(readOnly = true)
    public List<Image> findByStyleBoardId(Long StyleBoardId) {
        try {
            return imageRepository.findByStyleBoardId(StyleBoardId);
        } catch (Exception e) {
            ExceptionLogger.logException(e);
            throw new ImageException(HttpStatus.BAD_REQUEST.value() , "이미지 조회 실패" , e);
        }
    }
}



