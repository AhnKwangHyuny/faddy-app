package faddy.email.dto;

import faddy.global.exception.BadRequestException;
import faddy.global.exception.ExceptionCode;

/**
 * 지원되는 이메일 인증 타입을 정의합니다.
 * 현재는 Gmail과 Naver 이메일 서비스를 지원합니다.
 */
public enum EmailAuthType {
    GMAIL("gmail"),
    NAVER("naver");

    private final String value;

    EmailAuthType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * 이메일 주소에서 도메인을 추출하여 지원되는 이메일 타입인지 확인
     *
     * @param email 확인할 이메일 주소
     * @return 지원되는 이메일 타입이면 true, 아니면 false
     */
    public static boolean isValidEmailType(String email) {
        if (email == null || email.isEmpty() || !email.contains("@")) {
            return false;
        }

        String domain = email.substring(email.lastIndexOf("@") + 1).toLowerCase();
        return domain.contains("gmail.com") || domain.contains("naver.com");
    }

    /**
     * 이메일 주소로부터 적절한 메일 서비스 타입 문자열을 반환
     *
     * @param email 이메일 주소
     * @return 메일 서비스 타입 문자열 ("gmail" 또는 "naver")
     * @throws BadRequestException 지원되지 않는 이메일 도메인인 경우
     */
    public static String getMailTypeFromEmail(String email) {
        if (email == null || email.isEmpty() || !email.contains("@")) {
            throw new BadRequestException(ExceptionCode.INVALID_EMAIL_FORMAT);
        }

        String domain = email.substring(email.lastIndexOf("@") + 1).toLowerCase();

        if (domain.contains("gmail.com")) {
            return GMAIL.getValue();
        } else if (domain.contains("naver.com")) {
            return NAVER.getValue();
        } else {
            throw new BadRequestException(ExceptionCode.INVALID_EMAIL_FORMAT);
        }
    }
}