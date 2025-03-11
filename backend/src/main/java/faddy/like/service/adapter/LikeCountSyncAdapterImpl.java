package faddy.like.service.adapter;

import faddy.global.exception.DbSyncException;
import faddy.global.exception.ExceptionCode;
import faddy.like.service.adapter.useCase.LikeCountSyncAdapter;
import faddy.like.service.useCase.LikeRedisService;
import faddy.like.service.useCase.LikeService;
import faddy.like.type.ContentType;
import faddy.styleBoard.service.useCase.StyleBoardLoadService;
import faddy.styleBoardComment.service.useCase.GetStyleBoardCommentService;
import faddy.user.domain.User;
import faddy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikeCountSyncAdapterImpl implements LikeCountSyncAdapter {

    private final StyleBoardLoadService styleBoardLoadService;
    private final LikeRedisService likeRedisService;
    private final LikeService likeService;
    private final UserService userService;
    private final GetStyleBoardCommentService getStyleBoardCommentService;



    @Override
    @Transactional
    public void updateStyleBoardLikeInDatabase() {
        //모든 styleBoard 조회
        try {
            styleBoardLoadService.getAll().forEach(styleBoard -> {
                // redis에서 좋아요 누른 유저 ids 조회
                List<Long> userIds = likeRedisService.getLikedUserIds(styleBoard.getId(), ContentType.STYLE_BOARD);

                // userIds를 통해 users 조회
                List<User> users = userService.findUsersByUserIds(userIds);
                // users가 비어있으면 like 저장 없이 리턴 처리
                if (users.isEmpty()) {
                    return;
                }
                // userIds to like Entity 전환 후 batch 저장 수행
                likeService.batchSaveStyleBoardLikes(styleBoard, users);
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new DbSyncException(ExceptionCode.FAIL_DB_ASYNC_ERROR);
        }
    }


    @Override
    @Transactional
    public void updateStyleBoardCommentLikeInDatabase() {
        try {
            // 모든 StyleBoardComment 조회
            getStyleBoardCommentService.loadALlStyleBoardComment().forEach(styleBoardComment -> {
                // redis에서 좋아요 누른 유저 ids 조회
                List<Long> userIds = likeRedisService.getLikedUserIds(styleBoardComment.getId(), ContentType.STYLE_BOARD_COMMENT);

                // userIds를 통해 users 조회
                List<User> users = userService.findUsersByUserIds(userIds);

                // users가 비어있으면 like 저장 없이 리턴 처리
                if (users.isEmpty()) {
                    return;
                }

                // userIds to like Entity 전환 후 batch 저장 수행
                likeService.batchSaveStyleBoardCommentLikes(styleBoardComment, users);
            });

        } catch (Exception e) {
            throw new DbSyncException(ExceptionCode.FAIL_DB_ASYNC_ERROR);
        }
    }
}
