package faddy.global.annotation.user;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class NicknameValidator implements ConstraintValidator<ValidNickname, String> {
    private final Pattern pattern = Pattern.compile("^[\\p{L}\\p{N}]{3,12}$");

    @Override
    public boolean isValid(String nickname, ConstraintValidatorContext context) {
        if (nickname == null) {
            return false;
        }
        return pattern.matcher(nickname).matches();
    }
}