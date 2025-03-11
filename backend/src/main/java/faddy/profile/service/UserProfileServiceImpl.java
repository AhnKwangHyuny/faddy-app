package faddy.profile.service;

import faddy.global.exception.BadRequestException;
import faddy.global.exception.ExceptionCode;
import faddy.log.exception.ExceptionLogger;
import faddy.profile.domain.dto.response.ChatRoomUserProfileDTO;
import faddy.profile.domain.dto.response.UserProfileDTO;
import faddy.profile.repository.ProfileJpaRepository;
import faddy.profile.service.useCase.UserProfileService;
import faddy.user.domain.Profile;
import faddy.user.domain.User;
import faddy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final ProfileJpaRepository profileRepository;
    private final UserRepository userRepository;

    private final String DEFAULT_PROFILE_IMAGE_URL = "/default_profile_image.png";

    @Override
    @Transactional(readOnly = true)
    public Profile findProfileByUserid(Long userId) { // 추후 수정 예정
        return userRepository.findProfileById(userId)
                .orElse(null);

    }

    @Override
    public String findProfileImageUrlByUserId(Long userId) {

        Profile profile = findProfileByUserid(userId);

        // profile이 null이 아니라면 이미지 url을 반환
        if (profile != null) {
            return profile.getProfileImageUrl();
        }

        return DEFAULT_PROFILE_IMAGE_URL;
    }


    /**
     *  채팅방에 접속한 유저 프로파일 조회
     *  @param username : 유저 아이디
     *  @param roomId : 채팅방 아이디
     *  @return ChatRoomUserProfileDTO : 유저 프로필 정보
     */
    @Transactional(readOnly = true)
    public ChatRoomUserProfileDTO getChatRoomUserProfile(String roomId, String username) {
        try {
            // 유저 프로파일 조회
            User user = userRepository.findUserByUsernameWithProfile(username)
                    .orElseThrow(() -> new BadRequestException(ExceptionCode.INVALID_USER_ID));

            Profile userProfile = user.getProfile();

            // 유저 프로필이 없다면 오류 발생
            if (userProfile == null) {
                throw new BadRequestException(ExceptionCode.INVALID_USER_ID);
            }

            UserProfileDTO userProfileDTO = UserProfileDTO.builder()
                    .username(user.getUsername())
                    .nickname(user.getNickname())
                    .profileImageUrl(userProfile.getProfileImageUrl())
                    .level(userProfile.getUserLevel())
                    .build();

            return ChatRoomUserProfileDTO.builder()
                    .roomId(roomId)
                    .userProfileDTO(userProfileDTO)
                    .build();

        } catch (RuntimeException e) {
            ExceptionLogger.logException(e);
            throw new BadRequestException(ExceptionCode.INVALID_USER_ID);
        }
    }


    /**
     *  채팅방에 참여한 모든 유저 프로필 조회
     *  @param userIds : 유저 아이디 리스트
     *  @return List<ChatRoomUserProfileDTO> : 유저 프로필 정보 리스트
     */
    @Transactional(readOnly = true)
    public List<ChatRoomUserProfileDTO> findUsersWithProfileByUserIds(String roomId, List<Long> userIds) {
        try {
            List<User> users = userRepository.findUsersWithProfileByUserIds(userIds);

            List<ChatRoomUserProfileDTO> userProfiles = new ArrayList<>();
            for (User user : users) {
                Profile userProfile = user.getProfile();

                // 유저 프로필이 없다면 오류 발생
                if (userProfile == null) {
                    ExceptionLogger.logException(new BadRequestException(ExceptionCode.INVALID_USER_ID));
                    throw new BadRequestException(ExceptionCode.INVALID_USER_ID);
                }

                UserProfileDTO userProfileDTO = UserProfileDTO.builder()
                        .username(user.getUsername())
                        .nickname(user.getNickname())
                        .profileImageUrl(userProfile.getProfileImageUrl())
                        .level(userProfile.getUserLevel())
                        .build();

                ChatRoomUserProfileDTO chatRoomUserProfileDTO = ChatRoomUserProfileDTO.builder()
                        .roomId(roomId)
                        .userProfileDTO(userProfileDTO)
                        .build();

                userProfiles.add(chatRoomUserProfileDTO);
            }

            return userProfiles;

        } catch (RuntimeException e) {
            ExceptionLogger.logException(e);
            throw new BadRequestException(ExceptionCode.INVALID_USER_ID);
        }
    }

}
