package faddy.chat.dto.response;


import faddy.chat.dto.LastChatContentDto;
import faddy.chat.type.ChatRoomType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ChatRoomResponse(Long roomId , String title , String thumbnailImage , LastChatContentDto chatContentDto, int roomMemberCount , String createdAt , ChatRoomType type) {
}
