package faddy.image.presentation;

import faddy.image.dto.ImageRequestDto;
import faddy.image.dto.ImageResponseDto;
import faddy.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
public class ImageController {
    private final ImageService imageService;

    @DeleteMapping
    public ResponseEntity<Void> handleImageDelete(@RequestBody ImageRequestDto imageRequestDto) {
        imageService.deleteImage(imageRequestDto.getHashedName());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/multiple")
    public ResponseEntity<Void> handleImageListDelete(@RequestBody List<ImageRequestDto> images) {

        imageService.deleteImages(images);
        System.out.println("images = " + images);
        return ResponseEntity.noContent().build();
    }


}
