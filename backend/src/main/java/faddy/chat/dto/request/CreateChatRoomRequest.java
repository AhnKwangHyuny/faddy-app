package faddy.chat.dto.request;


import faddy.chat.type.ChatRoomType;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateChatRoomRequest {

    private ChatRoomType type;

    private String masterId;

    private List<String> memberIds;

}
