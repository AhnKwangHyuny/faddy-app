package faddy.chat.service;

import faddy.auth.jwt.Service.JwtUtil;
import faddy.chat.domain.ChatRoom;
import faddy.chat.dto.response.ChatMessageResponse;
import faddy.chat.type.ContentType;
import faddy.global.exception.AuthorizationException;
import faddy.user.service.UserIdEncryptionUtil;
import faddy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomSystemMessageService {

    private final LoadChatRoomService loadChatRoomService;
    private final ChatRoomValidationService chatRoomValidationService;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserIdEncryptionUtil userIdEncryptionUtil;

    private final String LOCATION =ChatRoomSystemMessageService.class.getName();

    @Async
    public ChatMessageResponse enterChatRoom(Long roomId, String authorization) {
        try {
            String token = jwtUtil.extractRawToken(authorization);
            Long userId = userService.getUserIdByAuthorization(token);

            //log로 token , userId 확인
            log.info("token : " + token  + " userId : " + userId);

            // 사용자가 채팅방에 있는지 확인
            if (!chatRoomValidationService.isUserInChatRoom(userId, roomId)) {
                throw new AuthorizationException(
                        HttpStatus.UNAUTHORIZED.value(),
                        LOCATION + "해당 채팅방에 접근할 수 없습니다.");
            }

            ChatRoom room = loadChatRoomService.getChatRoomById(roomId);
            String userNickname = userService.getNicknameById(userId); // 사용자 닉네임 가져오기

            return ChatMessageResponse.builder()
                    .content(userNickname + "님이 채팅방에 입장했습니다.")
                    .sender(userIdEncryptionUtil.encryptUserId(userId))
                    .type(ContentType.SYSTEM)
                    .createdAt(LocalDateTime.now()) // 현재 시간으로 설정
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
