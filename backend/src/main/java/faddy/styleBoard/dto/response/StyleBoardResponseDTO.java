package faddy.styleBoard.dto.response;


import faddy.styleBoard.domain.Category;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class StyleBoardResponseDTO {

    private Long boardId;

    private String title;

    private LocalDateTime createdAt;

    private UserProfileDTO userProfileDTO;

    private InteractionCountDTO interactionCountDTO;

    private Category category;
}
