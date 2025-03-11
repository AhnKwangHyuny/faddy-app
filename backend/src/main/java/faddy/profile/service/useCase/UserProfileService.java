package faddy.profile.service.useCase;

import faddy.profile.domain.dto.response.ChatRoomUserProfileDTO;
import faddy.user.domain.Profile;

import java.util.List;

public interface UserProfileService {

    /**
     * 사용자 ID로 프로필을 조회
     *
     * @param userId 사용자 ID
     * @return 프로필 객체
     */
    Profile findProfileByUserid(Long userId);

    /**
     * 사용자 ID로 프로필 이미지 URL을 조회
     *
     * @param userId 사용자 ID
     * @return 프로필 이미지 URL
     */
    String findProfileImageUrlByUserId(Long userId);

    /**
     * 채팅방에 참여한 모든 유저 프로필 조회
     *
     * @param roomId  채팅방 ID
     * @param userIds 유저 ID 리스트
     * @return 유저 프로필 정보 리스트
     */
    List<ChatRoomUserProfileDTO> findUsersWithProfileByUserIds(String roomId, List<Long> userIds);

    /**
     * 채팅방에 접속한 유저 프로파일 조회
     *
     * @param roomId   채팅방 ID
     * @param username 유저 아이디
     * @return 유저 프로필 정보
     */
    ChatRoomUserProfileDTO getChatRoomUserProfile(String roomId, String username);
}
