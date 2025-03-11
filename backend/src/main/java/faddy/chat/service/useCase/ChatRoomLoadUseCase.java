package faddy.chat.service.useCase;

import faddy.chat.domain.ChatRoom;
import faddy.chat.dto.response.ChatRoomResponse;

import java.util.List;

public interface ChatRoomLoadUseCase {
    ChatRoom getChatRoomById(Long roomId);

    List<ChatRoomResponse> getChatRooms(int page);

    List<ChatRoomResponse> getUserChatRooms(int page , Long userId);

}
