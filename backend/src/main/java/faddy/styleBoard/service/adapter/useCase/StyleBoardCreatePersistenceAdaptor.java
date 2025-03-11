package faddy.styleBoard.service.adapter.useCase;

import faddy.styleBoard.dto.request.StyleBoardRequestDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface StyleBoardCreatePersistenceAdaptor {

    /**
     *  스타일보드 생성 및 , 연관관계 매핑 , 저장 로직 수행
     *  @param StyleBoardRequestDTO 클라이언트에서 보낸 스타일보드 데이터 dto (content , category , title)
     *  @param request HttpServletRequest 클라이언트 request header
     *  @return styleBoardId
     * */

    Long create(StyleBoardRequestDTO styleBoardRequestDTO , HttpServletRequest request);
}
