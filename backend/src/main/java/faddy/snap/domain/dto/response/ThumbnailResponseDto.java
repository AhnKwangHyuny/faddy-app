package faddy.snap.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@JsonDeserialize(using = JsonDeserializer.class)
public class ThumbnailResponseDto {

    @NotNull
    private String snapId;

    @NotNull
    private String userId;

    @JsonProperty(defaultValue = "No description provided")
    private String description;

    @JsonProperty(defaultValue = "/test_image1.png")
    private String imageUrl;

    private List<String> hashTags;

    @JsonProperty(defaultValue = "Unknown")
    private String nickname;

    @JsonProperty(defaultValue = "/default_profile.jpg")
    private String profileImageUrl;

    public ThumbnailResponseDto(String snapId, String userId  ,String description,
                                String imageUrl, List<String> hashTags,
                                String nickname, String profileImageUrl) throws Exception {
        this.snapId = snapId;
        this.userId = userId;
        this.description = description;
        this.imageUrl = imageUrl;
        this.hashTags = hashTags;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}