package faddy.styleBoardComment.service.useCase;

import java.util.List;

public interface StyleBoardCommentRedisService {

    void initializeStyleBoardComments(Long styleBoardId);

    void saveStyleBoardComment(Long styleBoardId, Long styleBoardCommentId);

    void removeStyleBoardComment(Long styleBoardId, Long styleBoardCommentId);

    int countStyleBoardComments(Long styleBoardId);

    List<Long> getStyleBoardCommentIds(Long styleBoardId);

    void deleteStyleBoardComments(Long styleBoardId);
}
