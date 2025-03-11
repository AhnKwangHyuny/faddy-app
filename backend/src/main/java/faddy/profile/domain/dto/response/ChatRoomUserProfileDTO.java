package faddy.profile.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
@Builder
public class ChatRoomUserProfileDTO {
    @NonNull
    private String roomId;

    @NonNull
    private UserProfileDTO userProfileDTO;
}
