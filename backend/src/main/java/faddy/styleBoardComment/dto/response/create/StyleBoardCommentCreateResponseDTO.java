package faddy.styleBoardComment.dto.response.create;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class StyleBoardCommentCreateResponseDTO {
    private Long id;
    private UserDTO author;
    private String content;
    private LocalDateTime created_at;
    private int likeCount;
    private boolean isLiked;
}