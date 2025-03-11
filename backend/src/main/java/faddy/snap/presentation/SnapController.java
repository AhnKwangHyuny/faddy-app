package faddy.snap.presentation;

import faddy.api.Dto.ResponseDto;
import faddy.global.exception.ExceptionCode;
import faddy.global.exception.InternalServerException;
import faddy.global.exception.SnapException;
import faddy.snap.domain.Snap;
import faddy.snap.domain.dto.request.SnapRequestDto;
import faddy.snap.domain.dto.response.SnapResponseDto;
import faddy.snap.domain.dto.response.ThumbnailResponseDto;
import faddy.snap.infrastructure.mapper.ThumbnailMapper;
import faddy.snap.service.SnapService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/snaps")
@Api(tags = "Snap APIs")
public class SnapController {

    private final SnapService service;

    private final ThumbnailMapper thumbnailMapper;

    @ApiOperation(value = "Get snap data", notes = "Retrieve snap data from the database and send it to the client")
    @GetMapping("/detail")
    public ResponseEntity<ResponseDto> getSnapData(@RequestParam("snapId") String snapId) {
        System.out.println("snapId = " + snapId);
        try {
            SnapResponseDto snapResponseDto = service.getSnapResponseDtoWithSnapProjection(snapId);

            System.out.println("snapResponseDto.toString() = " + snapResponseDto.toString());

            return ResponseEntity.ok(createSuccessResponse(snapResponseDto));
        } catch (Exception e) {

            return createErrorResponse(e);
        }
    }

    /**
     * 페이징을 통해 썸네일 목록을 가져옵니다.
     *
     * @param page 페이지 번호 (기본값: 0)
     * @return 썸네일 목록 (페이지당 4개)
     */
    @GetMapping("/thumbnails")
    public ResponseEntity<ResponseDto> getThumbnails(@RequestParam(name = "page", defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, 4); // 4 snaps per request

        try {
            // db에 snap 데이터 요청 per page
            Page<Snap> snaps = service.getThumbnails(page);

            // snaps -> thumbnailResponseDto 로 변환
            List<ThumbnailResponseDto> response = snaps.stream()
                    .map(snap -> {
                        try {
                            return thumbnailMapper.mapSnapToThumbnailResponseDto(snap);
                        } catch (Exception e) {
                            throw new InternalServerException(ExceptionCode.MAPPING_ERROR_SNAP_TO_THUMBNAIL);
                        }
                    })
                    .toList();

            return ResponseEntity.ok().body(
                    new ResponseDto<List<ThumbnailResponseDto>> (
                            HttpStatus.OK.name(),
                            "페이징에 성공했습니다. body : thumbnaildata per page",
                            response
                    )
            );

        } catch (InternalServerException e) {
            return ResponseEntity.internalServerError().body(
                    new ResponseDto(
                            String.valueOf(e.getCode()),
                            e.getMessage()
                    )
            );
        }
    }


    private <T> ResponseDto<T> createSuccessResponse(T data) {
        return ResponseDto.success(data);
    }

    private ResponseEntity<ResponseDto> createErrorResponse(Exception e) {

        if (e instanceof SnapException) {
            SnapException snapException = (SnapException) e;

            logError(snapException);

            return ResponseEntity.status(snapException.getErrorCode())
                .body(new ResponseDto(
                        String.valueOf(snapException.getErrorCode()),
                        snapException.getMessage()
                )
            );
        } else {
            return ResponseEntity.internalServerError()
                    .body(new ResponseDto(
                        String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
        "An error occurred while processing the server."
                    )
                );
        }
    }

    private void logError(SnapException e) {
        log.warn("An error occurred while creating the snap response.", e);
    }


}
