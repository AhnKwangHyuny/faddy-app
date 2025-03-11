package faddy.like.presentation;

import faddy.global.response.ApiResponse;
import faddy.global.response.SuccessApiResponse;
import faddy.like.dto.response.LikeCountResponseDTO;
import faddy.like.service.useCase.LikeRedisService;
import faddy.like.type.ContentType;
import faddy.user.service.UserIdEncryptionUtil;
import faddy.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/likes")
@RequiredArgsConstructor
public class LikeTestController {

    private final UserService userService;
    private final UserIdEncryptionUtil userEncryptionUtil;
    private final LikeRedisService likeRedisService;

    @PostMapping
    public ResponseEntity<? extends ApiResponse> addLike(@RequestParam("objectType") String objectType, @RequestParam("objectId") Long objectId , HttpServletRequest request) {

        // userId 조회
        String token = request.getHeader("Authorization");
        String encryptedUserId = userService.findEncryptedUserId(token);
        Long userId = userEncryptionUtil.decryptUserId(encryptedUserId);

        //ContentType 조회
        ContentType contentType = ContentType.fromString(objectType);

        // 좋아요 등록
        likeRedisService.saveLike(objectId, userId, contentType);

        return SuccessApiResponse.of(HttpStatus.CREATED, "좋아요가 성공적으로 등록되었습니다.");
    }

    @DeleteMapping
    public ResponseEntity<? extends ApiResponse> removeLike(@RequestParam("objectType") String objectType, @RequestParam("objectId") Long objectId,  HttpServletRequest request) {

        // userId 조회
        String token = request.getHeader("Authorization");
        String encryptedUserId = userService.findEncryptedUserId(token);
        Long userId = userEncryptionUtil.decryptUserId(encryptedUserId);

        //ContentType 조회
        ContentType contentType = ContentType.fromString(objectType);

        // 좋아요 취소
        likeRedisService.removeLike(objectId, userId, contentType);


        return SuccessApiResponse.of(HttpStatus.OK, "좋아요가 성공적으로 취소되었습니다.");

    }

    @GetMapping("/count")
    public ResponseEntity<? extends ApiResponse> getLikeCount(@RequestParam("objectType") String objectType, @RequestParam("objectId") Long objectId) {

        int count = likeRedisService.countLikes(objectId, ContentType.fromString(objectType));

        LikeCountResponseDTO response = LikeCountResponseDTO.builder().count(count).build();

        return SuccessApiResponse.of(response);
    }
}
