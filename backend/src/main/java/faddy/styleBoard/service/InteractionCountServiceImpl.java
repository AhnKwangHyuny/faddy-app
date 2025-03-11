package faddy.styleBoard.service;

import faddy.global.exception.DeleteEntityException;
import faddy.like.service.useCase.LikeRedisService;
import faddy.like.type.ContentType;
import faddy.styleBoard.dto.response.InteractionCountDTO;
import faddy.styleBoard.service.useCase.InteractionCountService;
import faddy.styleBoardComment.service.useCase.StyleBoardCommentRedisService;
import faddy.views.service.useCase.ViewRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class InteractionCountServiceImpl implements InteractionCountService {

    private final ViewRedisService viewRedisService;
    private final LikeRedisService likeRedisService;
    private final StyleBoardCommentRedisService styleBoardCommentRedisService;

    @Override
    @Transactional(readOnly = true)
    public InteractionCountDTO getInteractionCount(Long styleBoardId) {

        ContentType LikeContentType = ContentType.STYLE_BOARD;
        faddy.views.type.ContentType ViewContentType = faddy.views.type.ContentType.STYLE_BOARD;

        //각각의 count 조회
        int viewCount = viewRedisService.countViews(styleBoardId, ViewContentType);
        int likeCount = likeRedisService.countLikes(styleBoardId, LikeContentType);
        int commentCount = styleBoardCommentRedisService.countStyleBoardComments(styleBoardId);

        return InteractionCountDTO.builder()
                .viewCount(viewCount)
                .likeCount(likeCount)
                .commentCount(commentCount)
                .build();
    }

    @Override
    @Transactional
    public void deleteStyleBoardInteractionCounts(Long styleBoardId) {
        try {

            ContentType likeContentType = ContentType.STYLE_BOARD;
            faddy.views.type.ContentType viewContentType = faddy.views.type.ContentType.STYLE_BOARD;

            likeRedisService.deleteLikes(styleBoardId, likeContentType);
            viewRedisService.deleteViews(styleBoardId, viewContentType);
            styleBoardCommentRedisService.deleteStyleBoardComments(styleBoardId);

        } catch (Exception e) {
            throw new DeleteEntityException(HttpStatus.BAD_REQUEST, e.getMessage(), styleBoardId);
        }
    }
}
