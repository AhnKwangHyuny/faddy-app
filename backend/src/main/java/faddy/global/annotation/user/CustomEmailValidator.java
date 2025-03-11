package faddy.global.annotation.user;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class CustomEmailValidator implements ConstraintValidator<CustomEmail, String> {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@(naver\\.com|gmail\\.com)$");

    @Override
    public void initialize(CustomEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        return value != null && EMAIL_PATTERN.matcher(value).matches();
    }
}