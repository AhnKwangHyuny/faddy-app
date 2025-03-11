package faddy.styleBoardComment.utils;

import faddy.styleBoardComment.domain.StyleBoardComment;
import faddy.styleBoardComment.dto.response.create.StyleBoardCommentCreateResponseDTO;
import faddy.styleBoardComment.dto.response.create.UserDTO;

public class StyleBoardCommentMapper {

    public static StyleBoardCommentCreateResponseDTO toDto(StyleBoardComment comment) {
        if (comment == null) {
            return null;
        }

        UserDTO user = UserDTO.builder()
                .level(comment.getAuthor().getProfile().getUserLevel())
                .profileImageUrl(comment.getAuthor().getProfile().getProfileImageUrl())
                .nickname(comment.getAuthor().getNickname())
                .build();

        return StyleBoardCommentCreateResponseDTO.builder()
                .id(comment.getId())
                .author(user)
                .content(comment.getContent())
                .created_at(comment.getCreated_at())
                .likeCount(0)
                .isLiked(false)
                .build();
    }


}
