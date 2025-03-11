package faddy.chat.dto.command;

import faddy.chat.domain.ChatRoom;
import faddy.chat.type.ContentType;
import lombok.Builder;

@Builder
public record ChatMessageCreateCommand(ChatRoom room , String content , Long sender , ContentType type) {

}
