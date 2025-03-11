package faddy.styleBoardComment.presentation;

import faddy.auth.jwt.Service.JwtUtil;
import faddy.global.response.ApiResponse;
import faddy.global.response.SuccessApiResponse;
import faddy.styleBoardComment.dto.request.StyleBoardCommentCreateRequestDTO;
import faddy.styleBoardComment.dto.response.create.StyleBoardCommentCreateResponseDTO;
import faddy.styleBoardComment.dto.response.find.StyleBoardCommentResponseDTO;
import faddy.styleBoardComment.dto.response.find.StyleBoardReplyResponseDTO;
import faddy.styleBoardComment.service.useCase.CreateStyleBoardCommentService;
import faddy.styleBoardComment.service.useCase.GetStyleBoardCommentService;
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
public class StyleBoardCommentController {

    private final CreateStyleBoardCommentService styleBoardCommentCreateService;
    private final GetStyleBoardCommentService getStyleBoardCommentService;

    private final JwtUtil jwtUtil;
    private final UserService userService;

    private final static String CREATE_SUCCESS_MESSAGE = "[create] 댓글이 성공적으로 등록되었습니다.";

    @Description("스타일보드 댓글 생성")
    @PostMapping("/detail/{styleBoardId}/comments")
    public ResponseEntity<? extends ApiResponse> createStyleBoardComment(HttpServletRequest request,
                                                                         @PathVariable("styleBoardId") Long styleBoardId,
                                                                         @RequestBody StyleBoardCommentCreateRequestDTO requestDto) {
        // 댓글 생성
        StyleBoardCommentCreateResponseDTO response = styleBoardCommentCreateService.create(requestDto, request, styleBoardId);

        return SuccessApiResponse.of(HttpStatus.CREATED, CREATE_SUCCESS_MESSAGE, response);
    }

    @Description("스타일보드 대댓글 생성")
    @PostMapping("/detail/{styleBoardId}/comments/{commentId}/replies")
    public ResponseEntity<? extends ApiResponse> createStyleBoardReply(HttpServletRequest request,
                                                                       @PathVariable("styleBoardId") String styleBoardId,
                                                                       @PathVariable("commentId") Long parentId,
                                                                       @RequestBody StyleBoardCommentCreateRequestDTO requestDto) {
        // 대댓글 생성
        StyleBoardReplyResponseDTO response = styleBoardCommentCreateService.createReply(requestDto, request, Long.valueOf(styleBoardId), parentId);

        return SuccessApiResponse.of(HttpStatus.CREATED, CREATE_SUCCESS_MESSAGE, response);
    }

    @Description("스타일보드 댓글 조회")
    @GetMapping("/detail/{styleBoardId}/comments")
    public ResponseEntity<? extends ApiResponse> getStyleBoardComments(@PathVariable("styleBoardId") Long styleBoardId , HttpServletRequest request) {

        String authorization = request.getHeader("Authorization");
        Long userId = null;

        //로그인 or 비로그인 유저 조회 구분
        if(authorization != null && jwtUtil.isExpired(authorization)) {
            userId = userService.getUserIdByAuthorization(authorization);
        }

        //댓글 조회
        List<StyleBoardCommentResponseDTO> response = getStyleBoardCommentService.findByStyleBoardIdWithReplies(styleBoardId , userId); // 비로그인 : userId = null

        return SuccessApiResponse.of(response);
    }

}
