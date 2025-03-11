package faddy.styleBoardComment.service.useCase;

import faddy.styleBoardComment.dto.request.StyleBoardCommentCreateRequestDTO;
import faddy.styleBoardComment.dto.response.create.StyleBoardCommentCreateResponseDTO;
import faddy.styleBoardComment.dto.response.find.StyleBoardReplyResponseDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface CreateStyleBoardCommentService {
    StyleBoardCommentCreateResponseDTO create(StyleBoardCommentCreateRequestDTO commentDto , HttpServletRequest request , Long styleBoardId);

    /**
     *  대댓글(Reply) 생성 # 대댓글은 댓글에 여러개 달 수 있다 하지만 대댓글에 또 대댓글을 달 수는 없다.
     * @param commentDto : 대댓글 생성 요청 DTO - content : 대댓글 내용
     * @param request : HttpServletRequest - authorization
     * @param styleBoardId : Long - 스타일보드 ID
     * @param parentId : Long - 부모 댓글 ID
     * */
    StyleBoardReplyResponseDTO createReply(StyleBoardCommentCreateRequestDTO commentDto , HttpServletRequest request , Long styleBoardId , Long parentId);
}
