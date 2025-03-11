package faddy.styleBoard.service.useCase;

import faddy.styleBoard.domain.StyleBoard;
import faddy.styleBoard.dto.request.StyleBoardCreateDTO;

public interface StyleBoardCreateService {

    /**
     *  스타일보드 엔팉  생성
     *  @param StyleBoardCreateDTO 스타일보드 생성 요청 DTO (title , content , category , hashTags)
     *  @return StyleBoard 스타일보드 엔티티
     * */
    StyleBoard createStyleBoardEntity(StyleBoardCreateDTO request);

}
