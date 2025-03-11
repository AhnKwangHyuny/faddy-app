package faddy.chat.service.adapter;

import faddy.chat.domain.ChatRoom;
import faddy.chat.dto.request.CreateChatRoomRequest;
import faddy.chat.dto.response.CreateChatRoomResponse;
import faddy.chat.service.ChatRoomCreateService;
import faddy.chat.service.chatRoomUserService.ChatRoomUserService;
import faddy.global.exception.ChatRoomException;
import faddy.log.exception.ExceptionLogger;
import faddy.profile.redis.useCase.RedisChatRoomUserProfileCacheService;
import faddy.profile.service.useCase.UserProfileService;
import faddy.user.repository.UserRepository;
import faddy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ChatRoomCreatePersistenceAdapter {

    private final ChatRoomCreateService chatRoomCreateService;
    private final ChatRoomUserService chatRoomUserService;
    private final UserRepository userRepository;
    private final UserProfileService userProfileService;
    private final RedisChatRoomUserProfileCacheService redisChatRoomUserProfileCacheService;
    private final UserService userService;

    @Transactional
    public CreateChatRoomResponse createChatRoom(CreateChatRoomRequest request) {
        try {
            // 메인 DB에 채팅방 생성
            ChatRoom room = createRoomInMainDB(request);

            // Redis에 채팅방 사용자 프로필 저장
            addUserProfilesToRedisCache(room, request);

            return CreateChatRoomResponse.builder().roomId(room.getId()).build();
        } catch (Exception e) {
            ExceptionLogger.logException(e);
            throw new ChatRoomException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "채팅방 생성에 실패했습니다.");
        }
    }

    private ChatRoom createRoomInMainDB(CreateChatRoomRequest request) {
        ChatRoom room = chatRoomCreateService.createChatRoom(request);
        List<Long> decryptedMemberIds = decryptMemberIds(request.getMasterId(), request.getMemberIds());
        String defaultTitle = generateChatRoomTitle(decryptedMemberIds);
        room.changeTitle(defaultTitle);
        chatRoomUserService.addUserToChatRoom(room, decryptedMemberIds);
        return room;
    }

    @Transactional
    private void addUserProfilesToRedisCache(ChatRoom room, CreateChatRoomRequest request) {
        try {
            List<Long> decryptedMemberIds = decryptMemberIds(request.getMasterId(), request.getMemberIds());
            // 빈 리스트라면 return (참여한 유저 0)
            if (decryptedMemberIds.isEmpty()) {
                return;
            }

            // 채팅방 사용자 프로필을 Redis에 저장
            userProfileService.findUsersWithProfileByUserIds(room.getId().toString() , decryptedMemberIds)
                    .forEach(user -> redisChatRoomUserProfileCacheService.setUserProfile(
                            user.getRoomId(),
                            user.getUserProfileDTO().getUsername(),
                            user.getUserProfileDTO().getNickname(),
                            user.getUserProfileDTO().getProfileImageUrl(),
                            user.getUserProfileDTO().getLevel()
                    ));

        } catch (Exception e) {
            ExceptionLogger.logException(e);
            throw new ChatRoomException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "채팅방 사용자 프로필을 Redis에 저장하는데 실패했습니다.");
        }
    }


    private List<Long> decryptMemberIds(String masterId, List<String> encryptedMemberIds) {
        List<String> memberIds = new ArrayList<>(encryptedMemberIds);
        memberIds.add(masterId);
        List<Long> decryptedMemberIds = new ArrayList<>(userService.decryptUserIds(memberIds));
        Collections.reverse(decryptedMemberIds);
        return decryptedMemberIds;
    }

    private String generateChatRoomTitle(List<Long> decryptedMemberIds) {
        List<String> nicknameList = userRepository.findNicknamesByUserIds(decryptedMemberIds);
        return String.join(", ", nicknameList);
    }
}
