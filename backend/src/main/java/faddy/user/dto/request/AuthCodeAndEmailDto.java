package faddy.user.dto.request;

import faddy.global.annotation.user.CustomEmail;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthCodeAndEmailDto {

    @CustomEmail
    @Valid
    private String email;

    @NotNull
    @Valid
    private String code;

}
