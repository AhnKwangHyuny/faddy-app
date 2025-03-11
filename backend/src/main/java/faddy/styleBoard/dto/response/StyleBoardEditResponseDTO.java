package faddy.styleBoard.dto.response;

import faddy.styleBoard.domain.Category;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class StyleBoardEditResponseDTO {

    private Long boardId;

    private String title;

    private String content;

    private List<String> hashTags;


}
