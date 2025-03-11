package faddy.global.annotation.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NicknameValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidNickname {
    String message() default "Invalid Nickname";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
