package faddy.views.presentation;

import faddy.global.response.ApiResponse;
import faddy.global.response.SuccessApiResponse;
import faddy.user.service.UserService;
import faddy.views.dto.response.ViewResponseDTO;
import faddy.views.service.useCase.ViewRedisService;
import faddy.views.type.ContentType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/views")
@RequiredArgsConstructor
public class ViewController {

    private final UserService userService;
    private final ViewRedisService viewRedisService;

//    @PostMapping
//    public ResponseEntity<? extends ApiResponse> createView(@RequestParam("objectType") String objectType, @RequestParam("objectId") Long objectId, HttpServletRequest request) {
//
//        //ContentType 조회
//        ContentType contentType = ContentType.fromString(objectType);
//
//        // 조회수 초기화
//        viewRedisService.initializeViews(objectId, contentType);
//
//        return SuccessApiResponse.of(HttpStatus.CREATED, "조회수가 성공적으로 등록되었습니다.");
//
//    }

    @PostMapping
    public ResponseEntity<? extends ApiResponse> countViews(@RequestParam("objectType") String objectType, @RequestParam("objectId") Long objectId ,HttpServletRequest request) {

        //ContentType 조회
        ContentType contentType = ContentType.fromString(objectType);

        // 유저 id 조회
        String token = request.getHeader("Authorization");
        Long userId = null;
        try {
            userId = userService.findUserIdByToken(token);
        } catch (IllegalArgumentException e) {
            // 토큰이 없는 경우
            userId = null;
        } catch (NullPointerException e) {
            // userId가 null인 경우
            userId = null;
        }

        viewRedisService.saveView(objectId, userId, contentType);

        // 조회수 반환
        int count = viewRedisService.countViews(objectId, contentType);

        ViewResponseDTO viewResponseDTO = ViewResponseDTO.builder()
                .views(count)
                .build();

        return SuccessApiResponse.of(viewResponseDTO);
    }


    @GetMapping
    public ResponseEntity<? extends ApiResponse> getviews(@RequestParam("objectType") String objectType, @RequestParam("objectId") Long objectId ,HttpServletRequest request) {

        //ContentType 조회
        ContentType contentType = ContentType.fromString(objectType);


        int views = viewRedisService.countViews(objectId, contentType);

        // 조회수 반환
        ViewResponseDTO viewResponseDTO = ViewResponseDTO.builder()
                .views(views)
                .build();

        return SuccessApiResponse.of(viewResponseDTO);
    }



}
