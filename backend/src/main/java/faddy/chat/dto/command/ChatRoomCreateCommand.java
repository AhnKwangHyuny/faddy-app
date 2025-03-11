package faddy.chat.dto.command;

import lombok.Builder;

@Builder
public record ChatRoomCreateCommand(String title , String master) {
}
