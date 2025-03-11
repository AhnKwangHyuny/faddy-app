package faddy.styleBoard.dto.request;

import faddy.styleBoard.domain.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class StyleBoardCreateDTO {

    @NotNull(message = "카테고리는 필수입니다.")
    private Category category;

    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 60, message = "제목은 최대 60자까지 입력 가능합니다.")
    private String title;

    @NotBlank(message = "내용은 필수입니다.")
    private String content;

}