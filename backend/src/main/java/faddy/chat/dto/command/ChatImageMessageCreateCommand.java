package faddy.chat.dto.command;

import faddy.chat.type.ContentType;
import lombok.Builder;

@Builder
public record ChatImageMessageCreateCommand(Long roomId , String content , Long sender , ContentType type) {

}
