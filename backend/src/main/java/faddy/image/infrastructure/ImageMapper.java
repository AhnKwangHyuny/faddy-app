package faddy.image.infrastructure;


import faddy.image.domain.Image;
import faddy.image.dto.ImageResponseDto;
import faddy.snap.domain.Snap;

public class ImageMapper {
    public static Image uploadImageResponseToEntity(ImageResponseDto response) {
        return new Image(response.getUrl(),
                response.getHashedName(),
                response.getOriginalName(),
                response.getSize(),
                response.getFormat(),
                response.getCategory());
    }
}
