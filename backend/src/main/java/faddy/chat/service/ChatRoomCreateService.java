package faddy.chat.service;

import faddy.chat.domain.ChatRoom;
import faddy.chat.dto.request.CreateChatRoomRequest;
import faddy.chat.dto.response.CreateChatRoomResponse;
import faddy.chat.repository.ChatRoomJpaRepository;
import faddy.chat.service.adapter.useCase.CreateChatRoomUseCase;
import faddy.global.exception.ChatRoomException;
import faddy.log.exception.ExceptionLogger;
import faddy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomCreateService implements CreateChatRoomUseCase {

    private final UserService userService;
    private final ChatRoomJpaRepository chatRoomRepository;

    @Override
    public ChatRoom createChatRoom(CreateChatRoomRequest request) {
        Long masterId = userService.decryptUserId(request.getMasterId());
        ChatRoom room = ChatRoom.createChatRoom(masterId, request.getType());

        if (request.getMemberIds() == null || request.getMemberIds().isEmpty()) {

            Exception e = new ChatRoomException(HttpStatus.BAD_REQUEST.value(), "대화상대가 존재하지 않습니다");

            ExceptionLogger.logException(e);

            throw new ChatRoomException(HttpStatus.BAD_REQUEST.value(), "대화상대가 존재하지 않습니다");
        }

        chatRoomRepository.save(room);
        return room;
    }
}
