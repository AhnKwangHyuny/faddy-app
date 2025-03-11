package faddy.hashTags.service;

import faddy.hashTags.domain.HashTag;
import faddy.hashTags.domain.dto.request.HashTagRequestDto;
import faddy.hashTags.domain.dto.response.HashTagIdResponseDto;
import faddy.hashTags.dto.request.HashTagRequestDTO;
import faddy.styleBoard.domain.StyleBoard;

import java.util.List;

public interface HashTagService {
    List<HashTagIdResponseDto> saveTags(HashTagRequestDto request);

    /**
     * 해쉬태그 리스트를 전달 받아 모두 엔티티로 매핑 후 엔티티 저장
     * @param hashTagRequestDTOs 해쉬태그 리스트
     * @return List<HashTag> 해쉬태그 엔티티 리스트
     * */
    List<HashTag> saveHashTags(List<HashTagRequestDTO> hashTagRequestDTOs);

    List<HashTag> findHashTagsByIds(List<Long> ids);

    /**
     * styleBoardId에 해당하는 해쉬태그 리스트를 반환
     * @param styleBoardId 스타일보드 아이디
     * @return List<HashTag> 해쉬태그 리스트
     * */
    List<HashTag> findHashTagsByStyleBoardId(Long styleBoardId);


    /**
     * styleBoardId에 해당하는 해쉬태그 리스트를 업데이트
     * @param styleBoard 스타일보드 아이디
     * @param hashTags 해쉬태그 리스트
     * */
    void updateHashTagsForStyleBoard(StyleBoard styleBoard, List<HashTagRequestDTO> hashTags);

    /**
     * 해당 object 삭제 시 연관 hashTag 삭제
     * @param objectId 삭제할 object id
     * */
    void deleteHashTagsByStyleBoardId(Long styleBoardId);

}
