package faddy.chat.service.chatRoomService;

import faddy.chat.domain.ChatRoom;
import faddy.chat.dto.request.UpdateChatRoomRequest;
import faddy.chat.dto.response.ChatRoomResponse;

public interface ChatRoomService {
    void updateChatRoom(Long roomId, UpdateChatRoomRequest request);
    void deleteChatRoom(Long roomId);

    ChatRoomResponse mapToChatRoomResponse(ChatRoom room);

}
