package faddy.chat.presentation;

import faddy.auth.jwt.Service.JwtUtil;
import faddy.chat.domain.Chat;
import faddy.chat.domain.ChatRoom;
import faddy.chat.dto.ErrorChatSenderDto;
import faddy.chat.dto.command.ChatMessageCreateCommand;
import faddy.chat.dto.request.AuthorizationTokenRequest;
import faddy.chat.dto.request.ChatMessageRequest;
import faddy.chat.dto.response.ChatMessageResponse;
import faddy.chat.service.ChatMessageCreateService;
import faddy.chat.service.ChatRoomSystemMessageService;
import faddy.chat.service.ChatRoomValidationService;
import faddy.chat.service.LoadChatRoomService;
import faddy.chat.type.ContentType;
import faddy.user.service.UserIdEncryptionUtil;
import faddy.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketController {

    private final LoadChatRoomService loadChatRoomService;
    private final ChatMessageCreateService chatMessageCreateService;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final SimpMessageSendingOperations messagingTemplate;
    private final UserIdEncryptionUtil userIdEncryptionUtil;
    private final ChatRoomValidationService chatRoomValidationService;
    private final ChatRoomSystemMessageService chatRoomSystemMessageService;

    private final String LOCATION = ChatWebSocketController.class.getName();

    @MessageMapping("/talks/{roomId}/send")
    @Async
    public void chat(@DestinationVariable("roomId") Long roomId,
                     @RequestBody @Valid ChatMessageRequest chatMessage) {
        try {
            String token = jwtUtil.extractRawToken(chatMessage.token());
            Long sender = userService.getUserIdByAuthorization(token);
            String username = userService.getUsernameByToken(token);

            ChatRoom room = loadChatRoomService.getChatRoomById(roomId);

            ChatMessageCreateCommand command = ChatMessageCreateCommand.builder()
                    .content(chatMessage.content())
                    .sender(sender)
                    .room(room)
                    .type(chatMessage.contentType())
                    .build();

            List<Chat> chats = chatMessageCreateService.createChatMessagesWithTimestamp(command);

            for (Chat chat : chats) {
                String senderName = chat.getType() == ContentType.TIMESTAMP || chat.getType() == ContentType.SYSTEM
                        ? "system"
                        : username;

                ChatMessageResponse response = ChatMessageResponse.builder()
                        .id(chat.getId())
                        .content(chat.getContent())
                        .sender(senderName)
                        .type(chat.getType())
                        .createdAt(chat.getCreated_at())
                        .build();

                messagingTemplate.convertAndSend("/sub/talks/" + roomId, response);
            }

        } catch (Exception e) {
            String token = jwtUtil.extractRawToken(chatMessage.token());
            Long sender = userService.getUserIdByAuthorization(token);
            String username = userService.getUsernameByToken(token);

            ErrorChatSenderDto senderDto = ErrorChatSenderDto.builder()
                    .sender(username)
                    .build();

            log.error(e.getMessage());
            ChatMessageResponse errorResponse = chatMessageCreateService.createErrorResponse(senderDto);
            messagingTemplate.convertAndSend("/sub/talks/" + roomId, errorResponse);
        }
    }

    // 채팅방 입장 시 시스템 메시지 전송
    @MessageMapping("/talks/{roomId}/enter")
    @Async
    public void enterChatRoom(@DestinationVariable("roomId") Long roomId,
                              @RequestBody @Valid AuthorizationTokenRequest authorization) {
        try {
            String token = authorization.token();

            ChatMessageResponse response = chatRoomSystemMessageService.enterChatRoom(roomId, token);
            messagingTemplate.convertAndSend("/sub/talks/" + roomId, response);
        } catch (Exception e) {
            log.error(e.getMessage());
            String token = authorization.token();
            String enteredUser = userService.getUsernameByToken(token);

            ErrorChatSenderDto senderDto = ErrorChatSenderDto.builder()
                    .sender(enteredUser)
                    .build();

            ChatMessageResponse errorResponse = chatMessageCreateService.createErrorResponse(senderDto);
            messagingTemplate.convertAndSend("/sub/talks/" + roomId, errorResponse);
        }
    }
}
