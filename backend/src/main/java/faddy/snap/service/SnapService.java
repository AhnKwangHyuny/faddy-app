package faddy.snap.service;

import faddy.global.Utils.EncryptionUtils;
import faddy.global.exception.ExceptionCode;
import faddy.global.exception.InternalServerException;
import faddy.global.exception.SnapException;
import faddy.snap.domain.Snap;
import faddy.snap.domain.dto.response.SnapResponseDto;
import faddy.snap.domain.dto.response.ThumbnailResponseDto;
import faddy.snap.infrastructure.mapper.SnapMapper;
import faddy.snap.repository.SnapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SnapService {

    private final SnapRepository snapRepository;
    private final SnapMapper snapMapper;

    /**
     * 활성화된 Snap을 ID로 조회
     * @param snapId 조회할 Snap의 ID
     * @return 조회된 Snap object
     * @throws SnapException 활성 상태의 Snap을 찾을 수 없는 경우 예외 발생
     */
    public Snap getActiveSnapById(Long snapId) {
        return snapRepository.findByIdAndDeletedAtIsNull(snapId)
            .orElseThrow(() ->
                    new SnapException(
                        HttpStatus.BAD_REQUEST.value() , "찾으시는 스냅이 존재하지 않습니다. {id : " + snapId + "}"
                )
        );
    }


    /**
     * snap detail page에 필요한 data 클라이언트로 반환
     * @param snapId 조회할 Snap의 ID
     * @return 조회된 Snap with user , profile , hashTags , images
     */
    @Transactional
    public SnapResponseDto getSnapResponseDtoWithSnapProjection(String snapId) throws Exception {

        // snapId 복호화
        Long rawId = EncryptionUtils.decryptEntityId(snapId);

        //해당 snapId를 가진 entity가 db에 저장되어있는지 확인
        validateSnapExists(rawId , snapId);

        //snapProjection 조회 by snapId
        Optional<Snap> snapOpt = snapRepository.findById(rawId);

        Snap snap = snapOpt.orElseThrow(() ->
                new SnapException(
                        HttpStatus.BAD_REQUEST.value(),
                        "존재하지 않는 스냅 입니다. (조회x)"
                )
        );

        // snap to responseDto
        return snapMapper.toDto(snap , snap.getSnapImages() , snap.getHashTags() , snap.getUser());
    };

    private void validateSnapExists(Long rawId, String encryptedSnapId) {

        boolean exists = snapRepository.existsByIdAndDeletedAtIsNull(rawId);
        if (!exists) {

            log.warn("raw snap ID : " + rawId);

            throw new SnapException(
                    HttpStatus.BAD_REQUEST.value(), "찾으시는 스냅이 존재하지 않습니다. {id : " + encryptedSnapId + "}"
            );
        }
    }


    /**
     * 페이징을 통해 썸네일 목록을 반환
     *
     * @param page 페이지 번호
     * @return 썸네일 목록 (페이지당 4개)
     */
    @Transactional(readOnly = true)
    public Page<Snap> getThumbnails(int page) {

        try {
            Pageable pageable = PageRequest.of(page, 4);

            return snapRepository.findAllSnapsByPage(pageable);

        } catch (Exception e ) {
            log.error(e.toString());

            throw new InternalServerException(ExceptionCode.FAIL_PAGING_ERROR);

        }

    }


    @Transactional(readOnly = true)
    public Snap getSnapById(Long snapId) {
        return snapRepository.findById(snapId)
                .orElseThrow(() -> new SnapException(
                        HttpStatus.BAD_REQUEST.value(),
                        "해당 스냅이 존재하지 않습니다. [snapId : " + snapId + "]"
                ));
    }

}

