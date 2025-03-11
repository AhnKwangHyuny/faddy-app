package faddy.global.Utils;

import java.util.regex.Pattern;

public class UserValidator {
    private static final Pattern ID_PATTERN = Pattern.compile("^[a-z0-9]{5,15}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$");

    public static boolean isValidUsername(String username) {
        return ID_PATTERN.matcher(username).matches();
    }

    public static boolean isValidPassword(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }
}
