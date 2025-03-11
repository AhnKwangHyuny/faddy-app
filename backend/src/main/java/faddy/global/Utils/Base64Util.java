package faddy.global.Utils;


import java.util.Base64;

public class Base64Util {
    public static boolean isBase64(String str) {
        try {
            Base64.getUrlDecoder().decode(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
