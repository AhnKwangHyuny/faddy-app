package faddy.profile.redis;

import faddy.chat.service.chatRoomUserService.ChatRoomUserService;
import faddy.log.exception.ExceptionLogger;
import faddy.profile.domain.UserLevel;
import faddy.profile.domain.dto.response.ChatRoomUserProfileDTO;
import faddy.profile.domain.dto.response.UserProfileDTO;
import faddy.profile.redis.useCase.RedisChatRoomUserProfileCacheService;
import faddy.profile.service.useCase.UserProfileService;
import faddy.user.domain.User;
import faddy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class RedisChatRoomUserProfileCacheServiceImpl implements RedisChatRoomUserProfileCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final UserProfileService userProfileService;
    private final ChatRoomUserService chatRoomUserService;
    private final UserService userService;

    private static final String REDIS_KEY_PREFIX = "chatRoom:";
    private static final String REDIS_KEY_USER_SUFFIX = ":user:";

    @Override
    public void setUserProfile(String roomId, String username, String nickname, String profileImageUrl, UserLevel level) {
        try {
            String key = buildRedisKey(roomId, username);
            Map<String, String> userProfile = Map.of(
                    "nickname", nickname,
                    "profileImageUrl", profileImageUrl,
                    "level", level.name()
            );

            redisTemplate.opsForHash().putAll(key, userProfile);
        } catch (Exception e) {
            logAndThrowRedisException("Redis에 유저 프로필 설정에 실패하였습니다.", e);
        }
    }

    @Override
    public Optional<ChatRoomUserProfileDTO> getUserProfile(String roomId, String username) {
        try {
            String key = buildRedisKey(roomId, username);
            Map<Object, Object> userProfile = redisTemplate.opsForHash().entries(key);

            // 캐시 미스일 경우 DB에서 조회하여 캐싱
            if (userProfile == null || userProfile.isEmpty()) {
                return Optional.ofNullable(fetchAndCacheUserProfile(roomId, username));
            }

            return Optional.ofNullable(convertMapToDto(roomId, username, userProfile));
        } catch (Exception e) {
            logAndThrowRedisException("Redis에서 유저 프로필 조회에 실패하였습니다.", e);
            return Optional.empty(); // 예외 발생 시 Optional.empty() 반환
        }
    }

    @Override
    public void deleteUserProfile(String roomId, String username) {
        try {
            String key = buildRedisKey(roomId, username);
            redisTemplate.delete(key);
        } catch (Exception e) {
            logAndThrowRedisException("Redis에서 유저 프로필 삭제에 실패하였습니다.", e);
        }
    }

    @Override
    public List<ChatRoomUserProfileDTO> getAllUserProfilesInChatRoom(String roomId) {
        try {
            List<ChatRoomUserProfileDTO> userProfiles = new ArrayList<>();
            String pattern = buildRedisPattern(roomId);

            Set<String> keys = redisTemplate.keys(pattern);

            // 만약 캐싱 미스(keys 존재 x) 시 서버에서 유저 프로필을 조회하여 캐싱
            if (keys == null || keys.isEmpty()) {
                return fetchAndCacheAllUserProfiles(roomId);
            }

            for (String userKey : keys) {
                String username = extractUsernameFromKey(userKey);
                Map<Object, Object> userProfile = redisTemplate.opsForHash().entries(userKey);
                userProfiles.add(convertMapToDto(roomId, username, userProfile));
            }

            return userProfiles;
        } catch (Exception e) {
            logAndThrowRedisException("Redis에서 모든 유저 프로필 조회에 실패하였습니다.", e);
            return new ArrayList<>(); // 예외 발생 시 빈 리스트 반환
        }
    }

    @Override
    public void deleteAllUserProfiles(String roomId) {
        try {
            String pattern = buildRedisPattern(roomId);
            redisTemplate.keys(pattern).forEach(redisTemplate::delete);
        } catch (Exception e) {
            logAndThrowRedisException("Redis에서 모든 유저 프로필 삭제에 실패하였습니다.", e);
        }
    }

    @Override
    public ChatRoomUserProfileDTO fetchAndCacheUserProfile(String roomId, String username) {
        try {
            User user = userService.getUserWithProfileByUsername(username);

            setUserProfile(roomId, username, user.getNickname(), user.getProfile().getProfileImageUrl(), user.getProfile().getUserLevel());

            UserProfileDTO userProfileDTO = UserProfileDTO.builder()
                    .username(username)
                    .nickname(user.getNickname())
                    .profileImageUrl(user.getProfile().getProfileImageUrl())
                    .level(user.getProfile().getUserLevel())
                    .build();

            return ChatRoomUserProfileDTO.builder()
                    .roomId(roomId)
                    .userProfileDTO(userProfileDTO)
                    .build();
        } catch (Exception e) {
            logAndThrowRedisException("유저 프로필을 조회하고 Redis에 캐싱하는데 실패하였습니다.", e);
            return null; // 이 라인은 예외 때문에 실행되지 않지만, 컴파일러 요구로 추가합니다.
        }
    }

    @Override
    public List<ChatRoomUserProfileDTO> fetchAndCacheAllUserProfiles(String roomId) {
        try {
            Long roomIdNum = Long.parseLong(roomId);
            List<Long> userIds = chatRoomUserService.getUserIdsByChatRoomId(roomIdNum);
            List<ChatRoomUserProfileDTO> profileDTOs = userProfileService.findUsersWithProfileByUserIds(roomId, userIds);

            // 서버에서 조회한 프로필 레디스에 저장
            profileDTOs.forEach(dto -> {
                UserProfileDTO userProfileDTO = dto.getUserProfileDTO();
                setUserProfile(roomId, userProfileDTO.getUsername(), userProfileDTO.getNickname(), userProfileDTO.getProfileImageUrl(), userProfileDTO.getLevel());
            });

            return profileDTOs;
        } catch (Exception e) {
            logAndThrowRedisException("모든 유저 프로필을 조회하고 Redis에 캐싱하는데 실패하였습니다.", e);
            return new ArrayList<>(); // 예외 발생 시 빈 리스트 반환
        }
    }

    private String buildRedisKey(String roomId, String username) {
        return REDIS_KEY_PREFIX + roomId + REDIS_KEY_USER_SUFFIX + username;
    }

    private String buildRedisPattern(String roomId) {
        return REDIS_KEY_PREFIX + roomId + REDIS_KEY_USER_SUFFIX + "*";
    }

    private String extractUsernameFromKey(String key) {
        return key.substring(key.lastIndexOf(REDIS_KEY_USER_SUFFIX) + REDIS_KEY_USER_SUFFIX.length());
    }

    private ChatRoomUserProfileDTO convertMapToDto(String roomId, String username, Map<Object, Object> userProfile) {
        if (userProfile == null || userProfile.isEmpty()) {
            return null;
        }

        UserProfileDTO userProfileDTO = UserProfileDTO.builder()
                .username(username)
                .nickname((String) userProfile.get("nickname"))
                .profileImageUrl((String) userProfile.get("profileImageUrl"))
                .level(UserLevel.valueOf((String) userProfile.get("level")))
                .build();

        return ChatRoomUserProfileDTO.builder()
                .roomId(roomId)
                .userProfileDTO(userProfileDTO)
                .build();
    }

    private void logAndThrowRedisException(String message, Exception e) {
        ExceptionLogger.logException(e);
        throw new RedisSystemException(message, e);
    }
}
