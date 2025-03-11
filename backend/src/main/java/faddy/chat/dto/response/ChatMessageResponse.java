package faddy.chat.dto.response;

import faddy.chat.type.ContentType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatMessageResponse(Long id, String content, String sender , ContentType type , LocalDateTime createdAt) {

}
