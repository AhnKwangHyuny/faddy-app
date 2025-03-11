package faddy.chat.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateChatRoomRequest {
    @NotBlank(message = "title은 필수 값입니다.")
    private String title;
}
