package faddy.styleBoard.service;

import faddy.global.exception.DeleteEntityException;
import faddy.hashTags.service.HashTagService;
import faddy.like.service.useCase.LikeService;
import faddy.like.type.ContentType;
import faddy.styleBoard.dto.response.CheckOwnerResponseDTO;
import faddy.styleBoard.repository.StyleBoardJpaRepository;
import faddy.styleBoard.service.useCase.InteractionCountService;
import faddy.styleBoard.service.useCase.StyleBoardDeleteService;
import faddy.styleBoard.service.useCase.StyleBoardDetailService;
import faddy.styleBoardComment.service.useCase.StyleBoardCommentDeleteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class StyleBoardDeleteServiceImpl implements StyleBoardDeleteService {

    private final StyleBoardDetailService styleBoardDetailService;
    private final InteractionCountService interactionCountService;
    private final HashTagService hashTagService;
    private final StyleBoardCommentDeleteService styleBoardCommentDeleteService;
    private final LikeService likeService;

    private final StyleBoardJpaRepository styleBoardRepository;

    @Override
    public void deleteStyleBoard(String authorization , Long styleBoardId) {
        try {
            // check styleBoard owner
            CheckOwnerResponseDTO isOwner = styleBoardDetailService.checkStyleBoardOwner(styleBoardId, authorization);

            if (!isOwner.isOwner()) {
                throw new DeleteEntityException(HttpStatus.FORBIDDEN , "해당 스타일보드의 작성자가 아닙니다." , styleBoardId);
            }

            //delete styleBoard hashTags
            hashTagService.deleteHashTagsByStyleBoardId(styleBoardId);

            // delete styleBoardComment data & interaction count data
            styleBoardCommentDeleteService.deleteAllAndInteractionCountByStyleBoardId(styleBoardId);

            // delete likes
            likeService.deleteLike(ContentType.STYLE_BOARD , styleBoardId);

            //delete interaction count data
            interactionCountService.deleteStyleBoardInteractionCounts(styleBoardId);

            // delete styleBoard
            styleBoardRepository.deleteById(styleBoardId);

        } catch (Exception e) {
            throw new DeleteEntityException(HttpStatus.BAD_REQUEST , e.getMessage() ,styleBoardId);
        }
    }


}
