package faddy.styleBoard.presentation;

import faddy.global.response.ApiResponse;
import faddy.global.response.ErrorApiResponse;
import faddy.global.response.SuccessApiResponse;
import faddy.styleBoard.dto.request.FilteredStyleBoardRequestDTO;
import faddy.styleBoard.dto.request.StyleBoardEditRequestDTO;
import faddy.styleBoard.dto.request.StyleBoardRequestDTO;
import faddy.styleBoard.dto.response.*;
import faddy.styleBoard.service.adapter.useCase.StyleBoardCreatePersistenceAdaptor;
import faddy.styleBoard.service.useCase.StyleBoardDeleteService;
import faddy.styleBoard.service.useCase.StyleBoardDetailService;
import faddy.styleBoard.service.useCase.StyleBoardEditService;
import faddy.styleBoard.service.useCase.StyleBoardLoadService;
import faddy.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/styleBoards")
public class StyleBoardController {

    private final StyleBoardCreatePersistenceAdaptor styleBoardCreatePersistenceAdaptor;
    private final StyleBoardDetailService styleBoardDetailService;
    private final StyleBoardLoadService styleBoardLoadService;
    private final StyleBoardEditService styleBoardEditService;
    private final StyleBoardDeleteService styleBoardDeleteService;
    private final UserService userService;
    private static final String CREATE_SUCCESS_MESSAGE = "[create] 게시글이 성공적으로 등록되었습니다.";
    private static final String CREATE_FAIL_MESSAGE = "[create] 게시글 등록에 실패했습니다.";
    private static final String DETAIL_SUCCESS_MESSAGE = "[detail] 게시글 상세 조회 성공";
    private static final String CHECK_OWNER_SUCCESS_MESSAGE = "[check] 작성자 확인 성공";
    private static final String CHECK_OWNER_FAIL_MESSAGE = "[check] 작성자 확인 실패";

    @Description("스타일보드 생성")
    @PostMapping("/create")
    public ResponseEntity<? extends ApiResponse> createStyleBoard(@RequestBody StyleBoardRequestDTO styleBoardRequestDTO,
                                                                  HttpServletRequest request) {
        try {
            Long styleBoardId = styleBoardCreatePersistenceAdaptor.create(styleBoardRequestDTO, request);
            StyleBoardCreateResponseDTO response = new StyleBoardCreateResponseDTO(styleBoardId);

            return SuccessApiResponse.of(HttpStatus.CREATED , CREATE_SUCCESS_MESSAGE , response);
        } catch (Exception e) {
            return ErrorApiResponse.of(HttpStatus.BAD_REQUEST, CREATE_FAIL_MESSAGE);
        }
    }


    @Description("스타일보드 edit 데이터 조회")
    @GetMapping("{styleBoardId}/edit")
    public ResponseEntity<? extends ApiResponse> getStyleBoard(@PathVariable("styleBoardId") Long styleBoardId) {
        StyleBoardEditResponseDTO response = styleBoardLoadService.getStyleBoardForEdit(styleBoardId);

        return SuccessApiResponse.of(HttpStatus.OK, "스타일보드 edit 조회 성공", response);
    }

    @Description("스타일보드 삭제")
    @DeleteMapping("/{styleBoardId}")
    public ResponseEntity<? extends ApiResponse> deleteStyleBoard(@PathVariable("styleBoardId") Long styleBoardId , HttpServletRequest request) {

        String authorization = request.getHeader("Authorization");
        styleBoardDeleteService.deleteStyleBoard(authorization , styleBoardId);

        return SuccessApiResponse.of(HttpStatus.OK, "스타일보드 삭제 성공");

    }

    @Description("스타일보드 수정")
    @PutMapping("/{styleBoardId}")
    public ResponseEntity<? extends ApiResponse> editStyleBoard(@RequestBody StyleBoardEditRequestDTO request , @PathVariable("styleBoardId") Long styleBoardId) {

        styleBoardEditService.updateStyleBoard(request , styleBoardId);

        return SuccessApiResponse.of(HttpStatus.OK, "스타일보드 수정 성공");

    }


    @Description("스타일보드 상세 페이지 데이터 조회")
    @GetMapping("/detail/{styleBoard_id}")
    public ResponseEntity<? extends ApiResponse> getStyleBoardDetail(@PathVariable("styleBoard_id") Long styleBoardId,
                                                                     @RequestParam(value = "category", required = true) String category,
                                                                     HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        Long viewerId = null;
        if(token != null) {
            viewerId = userService.findUserIdByToken(token);
        }

        StyleBoardDetailResponseDTO response = styleBoardDetailService.getStyleBoardDetailData(styleBoardId, category.toUpperCase(), viewerId);

        return SuccessApiResponse.of(HttpStatus.OK, DETAIL_SUCCESS_MESSAGE, response);
    }

    @Description("스타일보드 목록 조회")
    @PostMapping
    public ResponseEntity<? extends ApiResponse> getStyleBoards(@RequestBody FilteredStyleBoardRequestDTO request) {

        try {
            // DTO에서 파라미터 추출
            String sort = request.getSort();
            String category = request.getCategory();
            int page = request.getPage();
            int size = request.getSize();
            List<String> tags = request.getTags();


            List<StyleBoardResponseDTO> response = styleBoardLoadService.getFilteredStyleBoards(category, sort, tags, page, size);


            //styleBoardDto 리스트 조회
            System.out.println("response.size() = " + response.size());

            return SuccessApiResponse.of(HttpStatus.OK, "스타일보드 목록 조회 성공", response);

        } catch (Exception e) {
            e.printStackTrace();
            return ErrorApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, "스타일보드 목록 조회 실패");
        }
    }

    @Description("detail 페이지 접근자가 스타일보드 작성자인지 확인")
    @GetMapping("/detail/{styleBoardId}/check-owner")
    public ResponseEntity<? extends ApiResponse> checkStyleBoardOwner(@PathVariable("styleBoardId") Long styleBoardId,
                                                                      HttpServletRequest request) {
        // token 추출
        String token = request.getHeader("Authorization");

        // token 없으면 isOwner: false로 설정된 DTO 반환
        if (token == null) {
            CheckOwnerResponseDTO response = new CheckOwnerResponseDTO(false);
            return ErrorApiResponse.of(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다.", response);
        }

        CheckOwnerResponseDTO response = styleBoardDetailService.checkStyleBoardOwner(styleBoardId, token);

        return SuccessApiResponse.of(HttpStatus.OK, "소유자 확인 성공", response);
    }

}
