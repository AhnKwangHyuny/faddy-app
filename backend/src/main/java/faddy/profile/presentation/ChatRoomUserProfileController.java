package faddy.profile.presentation;

import faddy.global.response.ApiResponse;
import faddy.global.response.ErrorApiResponse;
import faddy.global.response.SuccessApiResponse;
import faddy.profile.domain.dto.response.ChatRoomUserProfileDTO;
import faddy.profile.redis.useCase.RedisChatRoomUserProfileCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class ChatRoomUserProfileController {

    private final RedisChatRoomUserProfileCacheService redisChatRoomUserProfileCacheService;

    /**
     * 특정 채팅방에서 단일 유저 프로필을 조회합니다.
     *
     * @param roomId  채팅방 ID
     * @param username 유저 이름
     * @return 유저 프로필
     */
    @GetMapping("/{roomId}/user/{username}/profile")
    public ResponseEntity<? extends ApiResponse> getUserProfile(@PathVariable("roomId") String roomId,
                                                                @PathVariable("username") String username) {

        Optional<ChatRoomUserProfileDTO> userProfile = redisChatRoomUserProfileCacheService.getUserProfile(roomId, username);

        if (userProfile.isPresent()) {
            return SuccessApiResponse.of(userProfile.get());
        } else {
            return ErrorApiResponse.of(HttpStatus.NO_CONTENT, "유저 프로필이 존재하지 않습니다.");
        }
    }

    /**
     * 특정 채팅방의 모든 유저 프로필을 조회합니다.
     *
     * @param roomId 채팅방 ID
     * @return 유저 프로필 목록
     */
    @GetMapping("/{roomId}/users/profiles")
    public ResponseEntity<? extends ApiResponse> getAllUserProfiles(@PathVariable("roomId") String roomId) {

        List<ChatRoomUserProfileDTO> userProfiles = redisChatRoomUserProfileCacheService.getAllUserProfilesInChatRoom(roomId);

        if (userProfiles != null && !userProfiles.isEmpty()) {
            return SuccessApiResponse.of(userProfiles);
        } else {
            return ErrorApiResponse.of(HttpStatus.NO_CONTENT, "유저 프로필이 존재하지 않습니다.");
        }
    }

    /**
     * 특정 채팅방의 단일 유저 프로필을 삭제합니다.
     *
     * @param roomId  채팅방 ID
     * @param username 유저 이름
     * @return 삭제 상태
     */
    @DeleteMapping("/{roomId}/user/{username}/profile")
    public ResponseEntity<? extends ApiResponse> deleteUserProfile(@PathVariable String roomId, @PathVariable String username) {
        redisChatRoomUserProfileCacheService.deleteUserProfile(roomId, username);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 특정 채팅방의 모든 유저 프로필을 삭제합니다.
     *
     * @param roomId 채팅방 ID
     * @return 삭제 상태
     */
    @DeleteMapping("/{roomId}/users/profiles")
    public ResponseEntity<? extends ApiResponse> deleteAllUserProfiles(@PathVariable String roomId) {
        redisChatRoomUserProfileCacheService.deleteAllUserProfiles(roomId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
