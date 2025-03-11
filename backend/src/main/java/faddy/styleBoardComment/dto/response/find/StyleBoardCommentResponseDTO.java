package faddy.styleBoardComment.dto.response.find;

import faddy.styleBoardComment.domain.StyleBoardComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StyleBoardCommentResponseDTO {

    private Long id;

    private String content;

    private boolean isDeleted;

    private UserDTO author;

    private LocalDateTime created_at;

    private List<StyleBoardReplyResponseDTO> replies;

    private int likeCount;

    private boolean isLiked;

    public static StyleBoardCommentResponseDTO from(StyleBoardComment comment, int likeCount , boolean isLiked) {
        return StyleBoardCommentResponseDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .isDeleted(comment.isDeleted())
                .author(UserDTO.from(comment.getAuthor()))
                .created_at(comment.getCreated_at())
                .likeCount(likeCount)
                .isLiked(isLiked)
                .build();
    }

    public StyleBoardCommentResponseDTO setReplies(List<StyleBoardReplyResponseDTO> replies) {
        this.replies = replies;
        return this;
    }
}
