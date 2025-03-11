package faddy.styleBoard.service.useCase;

import faddy.styleBoard.domain.StyleBoard;
import faddy.styleBoard.dto.response.StyleBoardEditResponseDTO;
import faddy.styleBoard.dto.response.StyleBoardResponseDTO;

import java.util.List;

public interface StyleBoardLoadService {

    /**
     *  서버에 저장된 모든 styleBoard 조회 후 반환
     *  @return list<StyleBoard> styleBoard 리스트
     * */
    List<StyleBoard> getAll();

    /**
     * 필터링 및 정렬 조건에 따라 styleBoard 조회 후 반환
     *
     * @param category  카테고리 필터
     * @param sort      정렬 기준
     * @param tags      태그 필터
     * @param page      페이지 번호
     * @param size      페이지 당 항목 수
     * @return 필터링 및 정렬된 styleBoard 리스트
     */
    List<StyleBoardResponseDTO> getFilteredStyleBoards(String category, String sort, List<String> tags, int page, int size);

    /**
     *  styleBoard 수정을 위한 styleBoardId에 해당하는 styleBoard 조회 후 반환
     *  @param styleBoardId  조회할 styleBoard의 id
     *  @return styleBoardEditResponseDTO styleBoard 수정을 위한 정보
     * */
    StyleBoardEditResponseDTO getStyleBoardForEdit(Long styleBoardId);

}
