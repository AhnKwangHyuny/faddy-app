package faddy.chat.dto;

import faddy.chat.type.ContentType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LastChatContentDto {
    String content;
    ContentType type;
}
