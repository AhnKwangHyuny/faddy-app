package faddy.user.dto.request;

import faddy.global.annotation.user.CustomEmail;
import faddy.global.annotation.user.ValidNickname;
import faddy.global.annotation.user.ValidPassword;
import faddy.global.annotation.user.ValidUserId;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupInfoDto {

    @CustomEmail
    private String email;

    @ValidNickname
    private String nickname;

    @ValidPassword
    private String password;

    @ValidUserId
    private String username;

}
