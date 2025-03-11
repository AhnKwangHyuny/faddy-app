package faddy.image.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ImageRequestDto {

    String hashedName;

    String url;

    public ImageRequestDto(String hashName, String url) {
        this.hashedName = hashName;
        this.url = url;
    }
}
