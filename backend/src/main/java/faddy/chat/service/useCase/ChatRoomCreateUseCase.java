package faddy.chat.service.useCase;

import faddy.chat.domain.ChatRoom;
import faddy.chat.dto.command.ChatRoomCreateCommand;
import faddy.chat.dto.request.CreateChatRoomRequest;

public interface ChatRoomCreateUseCase {
    ChatRoom createChatRoom(CreateChatRoomRequest request );
}
