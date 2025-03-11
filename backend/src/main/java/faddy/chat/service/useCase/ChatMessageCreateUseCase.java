package faddy.chat.service.useCase;

import faddy.chat.domain.Chat;
import faddy.chat.dto.ErrorChatSenderDto;
import faddy.chat.dto.command.ChatMessageCreateCommand;
import faddy.chat.dto.response.ChatMessageResponse;

import java.util.List;

public interface ChatMessageCreateUseCase {
    Chat createChatMessage(ChatMessageCreateCommand command);

    List<Chat> createChatMessagesWithTimestamp(ChatMessageCreateCommand command);
    ChatMessageResponse createErrorResponse(ErrorChatSenderDto errorChatSenderDto);
}
