package faddy.global.annotation.user;
import jakarta.persistence.Id;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class IdValidation implements ConstraintValidator<ValidUserId, String> {
    private final Pattern pattern = Pattern.compile("^[a-z0-9]{5,15}$");

    @Override
    public boolean isValid(String id, ConstraintValidatorContext context) {
        if (id == null) {
            return false;
        }
        return pattern.matcher(id).matches();
    }
}
