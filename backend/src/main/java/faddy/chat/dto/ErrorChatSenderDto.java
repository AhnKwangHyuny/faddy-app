package faddy.chat.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
public record ErrorChatSenderDto(String sender) {
}
