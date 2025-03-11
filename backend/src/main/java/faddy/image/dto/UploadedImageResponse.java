package faddy.image.dto;

import lombok.Getter;

@Getter
public class UploadedImageResponse {

    private final String hashFileName;

    private final String url;


    public UploadedImageResponse(String hashFileName, String url) {
        this.hashFileName = hashFileName;
        this.url = url;
    }

    public static UploadedImageResponse of(String hashFileName, String url) {
        return new UploadedImageResponse(hashFileName, url);
    }

}
