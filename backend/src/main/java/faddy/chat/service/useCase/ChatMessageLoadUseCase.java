package faddy.chat.service.useCase;

import faddy.chat.domain.ChatRoom;
import faddy.chat.dto.LastChatContentDto;
import faddy.chat.dto.response.ChatMessageResponse;

import java.util.List;

public interface ChatMessageLoadUseCase {
    List<ChatMessageResponse> loadChatsByChatRoom(ChatRoom room);

    LastChatContentDto loadLastChatMessage(Long roomId);

}
