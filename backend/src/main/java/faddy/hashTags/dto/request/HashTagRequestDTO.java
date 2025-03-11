package faddy.hashTags.dto.request;

import faddy.hashTags.types.ContentType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class HashTagRequestDTO {

    @NotBlank(message = "해시태그 이름은 필수입니다.")
    private String name;

    @Min(value = 0, message = "우선순위는 0 이상이어야 합니다.")
    @Max(value = 4, message = "우선순위는 4 이하여야 합니다.")
    private int priority;

    @NotNull(message = "콘텐츠 유형은 필수입니다.")
    private ContentType contentType;

    public HashTagRequestDTO(String name,ContentType contentType , int priority) {
        this.name = name;
        this.priority = priority;
        this.contentType = contentType;
    }
}
