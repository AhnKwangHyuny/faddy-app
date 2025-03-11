package faddy.hashTags.service;

import faddy.global.exception.BadRequestException;
import faddy.global.exception.DeleteEntityException;
import faddy.global.exception.ExceptionCode;
import faddy.global.exception.SaveEntityException;
import faddy.hashTags.domain.HashTag;
import faddy.hashTags.domain.dto.request.HashTagRequestDto;
import faddy.hashTags.domain.dto.response.HashTagIdResponseDto;
import faddy.hashTags.dto.request.HashTagRequestDTO;
import faddy.hashTags.repository.HashTagRepository;
import faddy.log.exception.ExceptionLogger;
import faddy.styleBoard.domain.StyleBoard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class HashTagServiceImpl implements HashTagService {
    private final HashTagRepository hashTagRepository;

    @Autowired
    public HashTagServiceImpl(HashTagRepository hashTagRepository) {
        this.hashTagRepository = hashTagRepository;
    }

    @Override
    @Transactional
    public List<HashTagIdResponseDto> saveTags(HashTagRequestDto request) {
        
        // request.getTags()로 태그 목록을 가져옴 
        List<String> hashTags = request.getTags();

        List<HashTag> hashTagEntities = IntStream.range(0, hashTags.size())
                .mapToObj(index -> {
                    String tagName = hashTags.get(index);
                    return new HashTag(tagName, index , request.getContentType());
                })
                .collect(Collectors.toList());

        
        List<HashTag> savedTags;
        
        try {
            savedTags = hashTagRepository.saveAll(hashTagEntities);

        } catch (Exception e) {
            e.printStackTrace(); // 예외 메시지 출력
            throw new SaveEntityException(ExceptionCode.FAIL_SAVE_ENTITY);
        }

        return savedTags.stream()
                .map(hashTag -> new HashTagIdResponseDto( hashTag.getId()))
                .collect(Collectors.toList());
    }


    public List<HashTag> findHashTagsByIds(List<Long> ids) {

        List<HashTag> foundHashTags = hashTagRepository.findByIdIn(ids);
        if (foundHashTags.size() != ids.size()) {
            throw new BadRequestException(400 , "존재하지 않는 태그가 리스트에 있습니다.");
        }
        return foundHashTags;
    }

    @Override
    @Transactional
    public List<HashTag> saveHashTags(List<HashTagRequestDTO> hashTagRequestDTOs) {
        if (hashTagRequestDTOs == null || hashTagRequestDTOs.isEmpty()) {
            return Collections.emptyList(); // 태그가 없을 시 빈 리스트 반환
        }

        try {
            List<HashTag> hashTagEntities = hashTagRequestDTOs.stream()
                    .map(hashTagRequestDTO -> new HashTag(
                            hashTagRequestDTO.getName(),
                            hashTagRequestDTO.getPriority(),
                            hashTagRequestDTO.getContentType()))
                    .collect(Collectors.toList());

            Optional<List<HashTag>> hashTags = Optional.ofNullable(hashTagRepository.saveAll(hashTagEntities));

            return hashTags.orElse(Collections.emptyList()); // 저장된 해시태그 리스트 반환

        } catch (Exception e) {
            ExceptionLogger.logException(e);
            throw new SaveEntityException(ExceptionCode.FAIL_CREATE_HASH_TAG);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<HashTag> findHashTagsByStyleBoardId(Long styleBoardId) {
        return hashTagRepository.findByStyleBoardId(styleBoardId);
    }


    @Override
    @Transactional
    public void updateHashTagsForStyleBoard(StyleBoard styleBoard, List<HashTagRequestDTO> newHashTags) {
        try {
            // 기존 해시태그 가져오기
            List<HashTag> existingHashTags = hashTagRepository.findByStyleBoardId(styleBoard.getId());

            // 새로운 해시태그 이름 목록
            List<String> newTagNames = newHashTags.stream()
                    .map(HashTagRequestDTO::getName)
                    .collect(Collectors.toList());

            // 기존 해시태그 중에서 새로운 해시태그 이름 목록에 없는 것들 삭제
            existingHashTags.forEach(existingTag -> {
                if (!newTagNames.contains(existingTag.getName())) {
                    try {
                        hashTagRepository.delete(existingTag);
                    } catch (Exception e) {
                        throw new SaveEntityException(ExceptionCode.FAIL_DELETE_ENTITY);
                    }
                }
            });

            // 새로운 해시태그 추가 및 업데이트
            IntStream.range(0, newHashTags.size())
                    .forEach(index -> {
                        HashTagRequestDTO newTag = newHashTags.get(index);
                        Optional<HashTag> existingTag = existingHashTags.stream()
                                .filter(tag -> tag.getName().equals(newTag.getName()))
                                .findFirst();

                        if (existingTag.isPresent()) {
                            // 기존 해시태그 업데이트
                            HashTag tag = existingTag.get();
                            tag.linkToStyleBoard(styleBoard);
                            tag.updatePriority(index);
                        } else {
                            // 새로운 해시태그 추가
                            HashTag tag = new HashTag(newTag.getName(), index, newTag.getContentType());
                            tag.linkToStyleBoard(styleBoard);
                            try {
                                hashTagRepository.save(tag);
                            } catch (Exception e) {
                                ExceptionLogger.logException(e);
                                throw new SaveEntityException(ExceptionCode.FAIL_SAVE_ENTITY);
                            }
                        }
                    });

        } catch (Exception e) {
            log.warn("updateHashTagsForStyleBoard error", e);
            throw new SaveEntityException(ExceptionCode.FAIL_SAVE_ENTITY);
        }
    }

    @Override
    public void deleteHashTagsByStyleBoardId(Long styleBoardId) {
        try {
            hashTagRepository.deleteByStyleBoardId(styleBoardId);


        } catch (Exception e) {
            ExceptionLogger.logException(e);
            throw new DeleteEntityException(HttpStatus.BAD_REQUEST.value() , "해시태그 삭제에 실패했습니다." , styleBoardId);
        }
    }
}
