package faddy.styleBoardComment.dto.response.create;

import faddy.profile.domain.UserLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private UserLevel level;
    private String profileImageUrl;
    private String nickname;
}