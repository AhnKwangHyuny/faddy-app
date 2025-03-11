package faddy.styleBoard.service;

import faddy.global.exception.SaveEntityException;
import faddy.global.exception.StyleBoardNotFoundException;
import faddy.hashTags.dto.request.HashTagRequestDTO;
import faddy.hashTags.service.HashTagService;
import faddy.hashTags.types.ContentType;
import faddy.styleBoard.domain.StyleBoard;
import faddy.styleBoard.dto.request.StyleBoardEditRequestDTO;
import faddy.styleBoard.repository.StyleBoardJpaRepository;
import faddy.styleBoard.service.useCase.StyleBoardEditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StyleBoardEditServiceImpl implements StyleBoardEditService {

    private final StyleBoardJpaRepository styleBoardRepository;

    private final HashTagService hashTagService;

    @Override
    @Transactional
    public void updateStyleBoard(StyleBoardEditRequestDTO request , Long styleBoardId) {

        try {
            StyleBoard styleBoard = styleBoardRepository.findById(styleBoardId)
                    .orElseThrow(() -> new StyleBoardNotFoundException(styleBoardId));

            // styleBoard update
            styleBoard.update(request.getTitle(), request.getContent());

            //hashTag update
            List<StyleBoardEditRequestDTO.HashTagDto> hashTags = request.getHashTags();
            List<HashTagRequestDTO> hashTagRequestDTOs = hashTags.stream()
                    .map(tag -> new HashTagRequestDTO(tag.getName(), ContentType.STYLE_BOARD, hashTags.indexOf(tag)))
                    .collect(Collectors.toList());

            // 해시태그 업데이트
            hashTagService.updateHashTagsForStyleBoard(styleBoard, hashTagRequestDTOs);


            styleBoardRepository.save(styleBoard);
        } catch (Exception e) {
            log.error("updateStyleBoard error", e);
            throw new SaveEntityException(HttpStatus.BAD_REQUEST , "styleBoard 업데이트에 실패했습니다.");
        }

    }


}