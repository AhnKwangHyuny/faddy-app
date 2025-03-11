package faddy.styleBoardComment.service.useCase;

public interface StyleBoardCommentDeleteService {

    /**
     *  styleBoardId에 해당하는 모든 댓글 및 interaction count 삭제
     *  @param styleBoardId 스타일보드 아이디
     * */
    void deleteAllAndInteractionCountByStyleBoardId(Long styleBoardId);

}
