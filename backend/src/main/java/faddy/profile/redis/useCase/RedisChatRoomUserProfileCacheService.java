package faddy.profile.redis.useCase;

import faddy.profile.domain.UserLevel;
import faddy.profile.domain.dto.response.ChatRoomUserProfileDTO;

import java.util.List;
import java.util.Optional;

/**
 * Redis 캐싱을 위한 채팅방 유저 프로필 서비스 인터페이스.
 */
public interface RedisChatRoomUserProfileCacheService {

    /**
     * 채팅방에 유저 프로필 저장.
     *
     * @param roomId          채팅방 ID
     * @param username        유저 이름
     * @param nickname        유저 닉네임
     * @param profileImageUrl 유저 프로필 이미지 URL
     * @param level           유저 레벨
     */
    void setUserProfile(String roomId, String username, String nickname, String profileImageUrl, UserLevel level);

    /**
     * 채팅방에서 유저 프로필 조회.
     *
     * @param roomId   채팅방 ID
     * @param username 유저 이름
     * @return 유저 프로필 정보
     */
    Optional<ChatRoomUserProfileDTO> getUserProfile(String roomId, String username);

    /**
     * 채팅방에서 특정 유저 프로필 삭제.
     *
     * @param roomId   채팅방 ID
     * @param username 유저 이름
     */
    void deleteUserProfile(String roomId, String username);

    /**
     * 채팅방의 모든 유저 프로필 조회.
     *
     * @param roomId 채팅방 ID
     * @return 채팅방의 모든 유저 프로필
     */
    List<ChatRoomUserProfileDTO> getAllUserProfilesInChatRoom(String roomId);

    /**
     * 채팅방의 모든 유저 프로필 삭제.
     *
     * @param roomId 채팅방 ID
     */
    void deleteAllUserProfiles(String roomId);

    /**
     * 캐싱 미스 시 서버에서 유저 프로필을 조회하여 캐싱.
     *
     * @param roomId   채팅방 ID
     * @param username 유저 이름
     * @return 서버에서 조회한 유저 프로필 정보
     */
    ChatRoomUserProfileDTO fetchAndCacheUserProfile(String roomId, String username);

    /**
     * 레디스 캐싱 미스 시 서버에서 모든 유저 프로필을 조회하여 캐싱.
     *
     * @param roomId 채팅방 ID
     * @return 서버에서 조회한 모든 유저 프로필 정보
     */
    List<ChatRoomUserProfileDTO> fetchAndCacheAllUserProfiles(String roomId);


}
