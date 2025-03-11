package faddy.snap.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import faddy.type.ContentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@ToString
public class CreateSnapRequestDto {

    @NotBlank(message = "사용자 ID는 비어 있을 수 없습니다.")
    @JsonProperty("userId")
    private String userId;

    @NotEmpty(message = "이미지 목록은 비어 있을 수 없습니다.")
    @JsonProperty("imageList")
    private List<ImageDto> imageList;

    @NotEmpty(message = "하나 이상의 해시태그 설정이 필요합니다.")
    @JsonProperty("hashTagIds")
    private List<HashTagIdDto> hashTagIds;

    @JsonProperty("categoryIds")
    private CategoryIds categoryIds;

    @Size(max = 1500, message = "설명은 1500자를 초과할 수 없습니다.")
    @JsonProperty("description")
    private String description;

    // ImageDto 클래스
    @Getter
    @NoArgsConstructor
    public static class ImageDto {

        @NotBlank(message = "해시된 이름은 비어 있을 수 없습니다.")
        @JsonProperty("hashedName")
        private String hashedName;

        @NotBlank(message = "이미지 URL은 비어 있을 수 없습니다.")
        @Size(max = 1024, message = "이미지 URL은 1024자를 초과할 수 없습니다.")
        @JsonProperty("url")
        private String url;
    }

    @Getter
    @NoArgsConstructor
    public static class CategoryIds {

        @JsonProperty("contentType")
        private ContentType contentType;

        @JsonProperty("linkedCategoryPairSet")
        private List<Map<String, Long>> linkedCategoryIds;

        public List<Long> getCategoryIdList() {
            if (linkedCategoryIds == null) {
                return Collections.emptyList();
            }

            return linkedCategoryIds.stream()
                    .flatMap(map -> map.values().stream())
                    .collect(Collectors.toList());
        }
    }

    @Getter
    @NoArgsConstructor
    public static class HashTagIdDto {

        @JsonProperty("id")
        private int id;
    }

    public List<String> getImageHashedNames() {
        return this.imageList.stream()
                .map(ImageDto::getHashedName)
                .collect(Collectors.toList());
    }

    public List<Long> getHashTagIds() {
        List<Long> hashTagIdsLong = new ArrayList<>();

        for (HashTagIdDto hashTagIdDto : this.hashTagIds) {
            Long id = Long.valueOf(hashTagIdDto.getId());
            hashTagIdsLong.add(id);
        }
        return hashTagIdsLong;
    }
}
