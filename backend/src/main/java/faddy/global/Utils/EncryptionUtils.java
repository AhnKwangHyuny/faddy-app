package faddy.global.Utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class EncryptionUtils {

    private static String SECRET_KEY;

    private EncryptionUtils(@Value("${util.secret-key}") String secretKey) {
        SECRET_KEY = secretKey;
    }

    public static String encryptEntityId(Long entityId) throws Exception {

        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encryptedBytes = cipher.doFinal(entityId.toString().getBytes());

        //암호화 된 아이디 url사용할 수 있도록 인코딩 (특수문자 , 공백 제거)
        return Base64.getUrlEncoder().encodeToString(encryptedBytes);
    }

    public static Long decryptEntityId(String encryptedEntityId) throws Exception {
        // base64 encoding인지 확인
        if (!Base64Util.isBase64(encryptedEntityId)) {
            throw new IllegalArgumentException("Invalid Base64 input string");
        }

        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] decryptedBytes = cipher.doFinal(Base64.getUrlDecoder().decode(encryptedEntityId));

        return Long.parseLong(new String(decryptedBytes));
    }


}