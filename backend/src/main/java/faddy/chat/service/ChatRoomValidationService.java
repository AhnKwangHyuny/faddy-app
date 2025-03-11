package faddy.chat.service;

import faddy.chat.repository.ChatRoomUserJpaRepository;
import faddy.global.exception.ChatRoomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomValidationService {

    private final ChatRoomUserJpaRepository chatRoomUserRepository;

    private final String LOCATION  = ChatRoomValidationService.class.getName();

    // 채팅방에 접근하는 user가 chatRoom의 등록된 유저인지 확인
    @Transactional(readOnly = true)
    public boolean isUserInChatRoom(Long userId, Long chatRoomId) {
        try {
            return chatRoomUserRepository.existsByUserAndChatRoom(userId, chatRoomId);
        } catch (Exception e) {

            System.out.println(" = " +LOCATION + " 채팅방 유저 조회 중 오류 발생" );
            throw new ChatRoomException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    LOCATION + " 채팅방 유저 조회 중 오류 발생");

        }
    }


}
