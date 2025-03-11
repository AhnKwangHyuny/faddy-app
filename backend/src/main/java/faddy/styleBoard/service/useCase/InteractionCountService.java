package faddy.styleBoard.service.useCase;

import faddy.styleBoard.dto.response.InteractionCountDTO;

public interface InteractionCountService {

    /**
     *  styleBoardId에 해당하는 styleBoard의 interactionCount 조회 후 반환 - likeCount, viewCount, commentCount
     *  @param styleBoardId  조회할 styleBoard의 id
     *  @return InteractionCountDTO styleBoard의 interactionCount
     * */
    InteractionCountDTO getInteractionCount(Long styleBoardId);

    /**
     *  styleBoardId에 해당하는 styleBoard의 모든 interactionCount 삭제 - likeCount, viewCount, commentCount
     *  @param styleBoardId  삭제할 styleBoard의 id
     * */
    void deleteStyleBoardInteractionCounts(Long styleBoardId);
}
