package faddy.like.presentation;

import faddy.api.Dto.ResponseDto;
import faddy.global.Utils.EncryptionUtils;
import faddy.like.service.useCase.LikeService;
import faddy.user.service.UserIdEncryptionUtil;
import faddy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/likes")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;
    private final UserService userService;
    private final UserIdEncryptionUtil userEncryptionUtil;

    @PostMapping("/{snapId}")
    public ResponseEntity<ResponseDto> like(@PathVariable("snapId") String snapId, @RequestHeader("Authorization") String authorization) throws Exception {
        Long decryptedSnapId = EncryptionUtils.decryptEntityId(snapId);
        Long userId = getUserIdFromToken(authorization);

        if(userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseDto.response("401", "유효하지 않은 토큰입니다."));
        }

        likeService.like(userId, decryptedSnapId);

        return ResponseEntity.ok().body(
            new ResponseDto(
                HttpStatus.OK.name(),
                "success like process "
            )
        );
    }


    @DeleteMapping("/{snapId}")
    public ResponseEntity<ResponseDto> unlike(@PathVariable("snapId") String snapId, @RequestHeader("Authorization") String authorization) throws Exception {

        Long decryptedSnapId = EncryptionUtils.decryptEntityId(snapId);
        Long userId = getUserIdFromToken(authorization);

        if(userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseDto.response("401", "유효하지 않은 토큰입니다."));
        }

        likeService.unlike(userId, decryptedSnapId);

        return ResponseEntity.ok().body(
                new ResponseDto(
                        HttpStatus.OK.name(),
                        "success unlike process "
                )
        );
    }

    @GetMapping("/{snapId}")
    public ResponseEntity<ResponseDto> getLikeCount(@PathVariable("snapId") String snapId) throws Exception {

        Long decryptedSnapId = EncryptionUtils.decryptEntityId(snapId);

        String likeCount = likeService.getLikeCount(decryptedSnapId);

        return ResponseEntity.ok().body(
                new ResponseDto(
                        HttpStatus.OK.name(),
                        "좋아요 수 가져오기 성공",
                        likeCount
                )
        );
    }

    private Long getUserIdFromToken(String authorization) {
        String encryptedUserId = userService.findEncryptedUserId(authorization);
        if (encryptedUserId == null) {
            return null;
        }
        return userEncryptionUtil.decryptUserId(encryptedUserId);
    }




}
