package faddy.email.service;

import faddy.api.response.AuthCodeVerificationResult;
import java.security.NoSuchAlgorithmException;

/**
 * 이메일 인증 서비스 인터페이스
 * 메일 전송, 인증 코드 검증, 이메일 중복 확인 등의 기능을 제공합니다.
 */
public interface MailService {

    /**
     * 사용자가 입력한 인증번호와 실제 인증 번호를 비교합니다.
     *
     * @param email 사용자 이메일
     * @param authNum 사용자가 입력한 인증번호
     * @return 인증 성공 여부
     */
    boolean checkAuthNum(String email, String authNum);

    /**
     * 회원 가입 인증 이메일을 생성하고 전송합니다.
     * 기본 메일 서비스를 사용합니다.
     *
     * @param email 인증 이메일을 받을 사용자의 이메일 주소
     * @return 생성된 인증 번호
     * @throws NoSuchAlgorithmException 암호화 알고리즘 예외 발생 시
     */
    String sendCodeToMail(String email) throws NoSuchAlgorithmException;

    /**
     * 회원 가입 인증 이메일을 생성하고 전송합니다.
     * 지정된 메일 서비스를 사용합니다.
     *
     * @param email 인증 이메일을 받을 사용자의 이메일 주소
     * @param mailType 사용할 메일 서비스 유형 ("naver" 또는 "gmail")
     * @return 생성된 인증 번호
     * @throws NoSuchAlgorithmException 암호화 알고리즘 예외 발생 시
     */
    String sendCodeToMail(String email, String mailType) throws NoSuchAlgorithmException;

    /**
     * 이메일 형식인지 확인합니다.
     *
     * @param email 검증할 이메일 주소
     * @return 유효한 이메일 형식이면 true, 그렇지 않으면 false
     */
    boolean isValidEmail(String email);

    /**
     * Redis에서 인증 코드를 삭제합니다.
     *
     * @param email 사용자 이메일
     */
    void deleteAuthCode(final String email);

    /**
     * Redis에 저장될 인증 코드의 키를 생성합니다.
     *
     * @param email 사용자 이메일
     * @return 생성된 키
     */
    String createKey(String email);

    /**
     * 임의의 인증 코드를 생성합니다.
     *
     * @return 생성된 인증 코드
     * @throws NoSuchAlgorithmException 암호화 알고리즘 예외 발생 시
     */
    String createCode() throws NoSuchAlgorithmException;

    /**
     * 이메일 중복을 확인합니다.
     *
     * @param email 중복 확인할 이메일
     */
    void checkDuplicatedEmail(String email);

    /**
     * 인증 코드를 검증합니다.
     *
     * @param email 사용자 이메일
     * @param authCode 인증 코드
     * @return 인증 결과
     */
    AuthCodeVerificationResult verifiedCode(final String email, final String authCode);

    /**
     * 이메일 중복 여부를 확인합니다.
     * Redis와 DB 모두에서 중복을 확인합니다.
     *
     * @param email 중복 확인할 이메일
     */
    void checkDuplication(String email);
}