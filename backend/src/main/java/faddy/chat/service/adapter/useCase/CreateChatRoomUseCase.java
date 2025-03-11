package faddy.chat.service.adapter.useCase;

import faddy.chat.domain.ChatRoom;
import faddy.chat.dto.request.CreateChatRoomRequest;
import faddy.chat.dto.response.CreateChatRoomResponse;

public interface CreateChatRoomUseCase {

    ChatRoom createChatRoom(CreateChatRoomRequest request);
}
