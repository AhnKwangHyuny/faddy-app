package faddy.styleBoard.dto.request;

import faddy.styleBoard.domain.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class StyleBoardRequestDTO {

    @NotNull(message = "카테고리는 필수입니다.")
    private Category category;

    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 60, message = "제목은 최대 60자까지 입력 가능합니다.")
    private String title;

    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    private List<HashTagDto> hashTags; // 해시태그 목록

    @Getter
    @Setter
    @NoArgsConstructor
    public static class HashTagDto {
        private String name; // 해시태그의 이름
    }

    // 모든 필드를 포함하는 생성자
    public StyleBoardRequestDTO(Category category, String title, String content, List<HashTagDto> hashTags) {
        this.category = category;
        this.title = title;
        this.content = content;
        this.hashTags = hashTags;
    }

}