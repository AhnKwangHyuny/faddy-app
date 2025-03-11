package faddy.image.service;

import faddy.image.domain.Image;
import faddy.image.dto.ImageRequestDto;
import faddy.image.dto.ImageResponseDto;
import faddy.image.dto.request.ImageLookupRequestDTO;
import faddy.image.type.ImageCategory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {


    ImageResponseDto uploadImage (MultipartFile file , ImageCategory category) throws IOException;

    void saveImage(ImageResponseDto dto);

    void validateImageFile(MultipartFile file);

    void deleteImage(String hashName);

    void deleteImages(List<ImageRequestDto> images);

    Image findById(Long imageId);

    List<Image> findByHashedNames(List<String> hashedNames);

    /**
     *  이미지 URL로 이미지 조회
     *  @param imageUrls 이미지 URL 목록
     *  @return Image 이미지 엔티티 리스트
     * */
    List<Image> findByImageUrl(List<ImageLookupRequestDTO> imageUrls);

    /**
     *  styleBoardId로 이미지 조회
     *  @param Long styleBoardId
     *  @return Image 이미지 엔티티 리스트
     * */
    List<Image> findByStyleBoardId(Long StyleBoardId);

}
