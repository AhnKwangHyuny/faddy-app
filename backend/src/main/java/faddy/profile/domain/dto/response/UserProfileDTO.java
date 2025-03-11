package faddy.profile.domain.dto.response;

import faddy.profile.domain.UserLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserProfileDTO {
    private String username;
    private String nickname;
    private String profileImageUrl;
    private UserLevel level;
}
