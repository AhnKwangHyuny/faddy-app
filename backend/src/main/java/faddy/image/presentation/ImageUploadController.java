package faddy.image.presentation;

import faddy.global.exception.ExceptionCode;
import faddy.global.exception.ImageException;
import faddy.image.dto.ImageResponseDto;
import faddy.image.dto.UploadedImageResponse;
import faddy.image.service.ImageService;
import faddy.image.type.ImageCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
public class ImageUploadController {

    private final ImageService imageService;

    @PostMapping
    public ResponseEntity<ImageResponseDto> handleImageUpload(@RequestParam("category") String category,
             @RequestParam("image") MultipartFile file) throws IOException {
        // 파일 크기 검증 (10MB)
        if (file.getSize() > 10_000_000) { // 10MB
            throw new ImageException(ExceptionCode.FILE_SIZE_TOO_LARGE);
        }

        //이미지 카테고리 존재 유무 확인
        if (category.isEmpty()) throw new ImageException(ExceptionCode.NOT_FOUND_IMAGE_CATEGORY);

        // 파일 형식 검증 (JPEG, PNG)
        String contentType = file.getContentType();
        if (contentType == null ||
                !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
            throw new ImageException(ExceptionCode.INVALID_AUTH_CODE);
        }

        // 소문자 -> 대문자로 변경 (snap -> SNAP)
        ImageCategory imageCategory = ImageCategory.valueOf(category.toUpperCase());
        ImageResponseDto imageResponseDto = imageService.uploadImage(file , imageCategory);

        UploadedImageResponse.of(imageResponseDto.getHashedName(), imageResponseDto.getUrl() );

        return ResponseEntity.created(URI.create(imageResponseDto.getUrl())).body(imageResponseDto);
    }

}
