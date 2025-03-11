package faddy.follows.presentation;

import faddy.api.Dto.ResponseDto;
import faddy.auth.jwt.Service.JwtUtil;
import faddy.follows.domain.dto.request.FollowRequestDto;
import faddy.follows.domain.dto.response.FollowResponseDto;
import faddy.follows.service.FollowService;
import faddy.user.domain.User;
import faddy.user.dto.request.UserIdRequestDto;
import faddy.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/users")
public class FollowController {
    private final UserService userService;
    private final FollowService followService;
    private final JwtUtil jwtUtil;

    /**
     * 친구 맺기(팔로잉)
     */
    @PostMapping("/follow")
    public ResponseEntity follow(HttpServletRequest request, @RequestBody FollowRequestDto requestDto) {

        String token = jwtUtil.getTokenFromRequest(request);
        try {

            //username을 통해 user 조회
            User following = userService.findUserByToken(token);
            User follower = userService.findByUsername(requestDto.getToUsername());

            String message = followService.follow(following, follower);

            return ResponseEntity.ok().body(
                    new ResponseDto(
                        String.valueOf(HttpStatus.OK.value()),
                            message
                    )
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new ResponseDto (
                            String.valueOf(HttpStatus.BAD_REQUEST.value()),
                            e.getMessage()
                    )
            );
        }
    }

    /**
     * 언팔하기
     */
    @DeleteMapping("/follow/{toUsername}")
    public ResponseEntity<ResponseDto<String>> deleteFollow(HttpServletRequest request , @PathVariable("toUsername") String toUsername){

        // authorization token 가져오기
        String token = jwtUtil.getTokenFromRequest(request);

        // 본인과 언팔할 유저 아이디 찾기
        try {
            User following = userService.findUserByToken(token);
            User follower = userService.findByUsername(toUsername);

            // 언팔로우
            followService.cancelFollow(following , follower);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new ResponseDto (
                            String.valueOf(HttpStatus.BAD_REQUEST.value()),
                            e.getMessage()
                    )
            );
        }

        return ResponseEntity.ok().body(
                new ResponseDto<String>(
                        String.valueOf(HttpStatus.OK.value()),
                        toUsername + "과의 언팔에 성공했습니다."
                )
        );
    }

    /**
     *  특정 팔로워 조회
     * */
    @GetMapping("/follow/{toUsername}")
    public ResponseEntity<ResponseDto> CheckFollowStatus(HttpServletRequest request ,@PathVariable("toUsername") String toUsername ) {
        // authorization token 가져오기
        String token = jwtUtil.getTokenFromRequest(request);

        // 본인과 언팔할 유저 아이디 찾기
        try {
            User following = userService.findUserByToken(token);
            User follower = userService.findByUsername(toUsername);

            // follow status 확인
            return ResponseEntity.ok().body(
                    new ResponseDto<Map<String, Boolean>>(
                            String.valueOf(HttpStatus.OK.value()),
                            "success",
                            Map.of("isFollowed", followService.isFollowed(following, follower))
                    )
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new ResponseDto (
                            String.valueOf(HttpStatus.BAD_REQUEST.value()),
                            e.getMessage()
                    )
            );
        }
    }

    /**
     * 팔로워 조회
     */
    @GetMapping("/{userId}/follower")
    public ResponseEntity<ResponseDto<List<FollowResponseDto>>> getFollowerList(@PathVariable("userId") String userId) {
        try {
            List<FollowResponseDto> followers = followService.getFollowers(userId);
            return ResponseEntity.ok().body(new ResponseDto<>(
                    String.valueOf(HttpStatus.OK.value()),
                    "success",
                    followers
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto<>(
                    String.valueOf(HttpStatus.BAD_REQUEST.value()),
                    e.getMessage(),
                    null
            ));
        }
    }

    /**
     * 팔로잉 조회
     */
    @GetMapping("/{userId}/following")
    public ResponseEntity<ResponseDto<List<FollowResponseDto>>> getFollowingList(@PathVariable("userId") String userId) {
        try {
            List<FollowResponseDto> followings = followService.getFollowings(userId);
            return ResponseEntity.ok().body(new ResponseDto<>(
                    String.valueOf(HttpStatus.OK.value()),
                    "success",
                    followings
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto<>(
                    String.valueOf(HttpStatus.BAD_REQUEST.value()),
                    e.getMessage(),
                    null
            ));
        }
    }

}