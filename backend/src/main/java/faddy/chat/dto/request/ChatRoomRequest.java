package faddy.chat.dto.request;

import lombok.Builder;

@Builder
public record ChatRoomRequest(Long roomId) {
}
