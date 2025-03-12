package faddy.email.service;

import faddy.email.dto.AuthCodeMessage;
import faddy.email.dto.EmailAuthType;
import faddy.global.Utils.RedisUtil;
import faddy.api.response.AuthCodeVerificationResult;
import faddy.global.exception.BadRequestException;
import faddy.global.exception.ExceptionCode;
import faddy.global.exception.ServerProcessingException;
import faddy.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.rmi.ServerError;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MailService 인터페이스의 구현 클래스
 * Naver와 Gmail 두 가지 메일 서비스를 사용하여 이메일 인증 기능을 제공합니다.
 */
@Slf4j
@Service
@Transactional
public class MailServiceImpl implements MailService {

    private static final String AUTH_CODE_PREFIX = "AuthCode";
    private static final int AUTH_CODE_EXPIRY_SECONDS = 210; // 3분 30초
    private static final int AUTH_CODE_LENGTH = 6;

    private final JavaMailSender naverMailSender;
    private final JavaMailSender gmailMailSender;
    private final RedisUtil redisUtil;
    private final UserRepository userRepository;

    public MailServiceImpl(
            @Qualifier("naverSender") JavaMailSender naverMailSender,
            @Qualifier("gmailSender") JavaMailSender gmailMailSender,
            RedisUtil redisUtil,
            UserRepository userRepository) {
        this.naverMailSender = naverMailSender;
        this.gmailMailSender = gmailMailSender;
        this.redisUtil = redisUtil;
        this.userRepository = userRepository;
    }

    /**
     * 메일 서비스 유형에 따라 적절한 JavaMailSender를 반환합니다.
     *
     * @param type 메일 서비스 유형 ("naver" 또는 "gmail")
     * @return 선택된 JavaMailSender 인스턴스
     */
    private JavaMailSender getMailSender(String type) {
        return "gmail".equalsIgnoreCase(type) ? gmailMailSender : naverMailSender;
    }

    @Override
    public boolean checkAuthNum(String email, String authNum) {
        String key = AUTH_CODE_PREFIX + email;
        String storedCode = redisUtil.getData(key);
        return storedCode != null && storedCode.equals(authNum);
    }

    /**
     * 이메일 주소를 분석하여 적절한 메일 서비스로 인증 코드를 전송합
     * 이메일 도메인에 따라 Gmail 또는 Naver 서비스를 사용
     *
     * @param email 인증 코드를 받을 이메일 주소
     * @return 생성된 인증 코드
     */
    @Override
    @Transactional
    public String sendCodeToMail(String email) throws NoSuchAlgorithmException {
        // 이메일 형식 검증
        if (!isValidEmail(email)) {
            throw new BadRequestException(ExceptionCode.INVALID_EMAIL_FORMAT);
        }

        // 지원되는 이메일 타입인지 확인
        if (!EmailAuthType.isValidEmailType(email)) {
            throw new BadRequestException(ExceptionCode.INVALID_EMAIL_FORMAT);
        }

        // 이메일로부터 메일 타입 문자열 얻기
        String mailType = EmailAuthType.getMailTypeFromEmail(email);

        // 결정된 메일 타입으로 인증 코드 전송
        return sendCodeToMail(email, mailType);
    }


    @Override
    @Transactional
    public String sendCodeToMail(String email, String mailType) throws NoSuchAlgorithmException {
        // 이메일 형식 검증
        if (!isValidEmail(email)) {
            throw new BadRequestException(ExceptionCode.INVALID_EMAIL_FORMAT);
        }

        String authCode = createCode();
        AuthCodeMessage content = AuthCodeMessage.createMessage(email, authCode);

        try {
            // 선택된 메일 서비스를 사용하여 이메일 전송
            JavaMailSender selectedMailSender = getMailSender(mailType);
            sendMail(selectedMailSender, content.getSetFrom(), content.getToMail(),
                    content.getTitle(), content.getContent());

            // Redis에 인증 코드 저장
            storeAuthCode(email, authCode);
            return authCode;
        } catch (Exception e) {
            log.error("메일 전송 중 오류 발생: {}", e.getMessage(), e);
            throw new ServerProcessingException(ExceptionCode.INVALID_EMAIL_AUTH_CODE);
        }
    }

    /**
     * 이메일을 전송합니다.
     *
     * @param mailSender 사용할 메일 서비스
     * @param setFrom 발신자의 이메일 주소
     * @param toMail 수신자의 이메일 주소
     * @param title 이메일의 제목
     * @param content 이메일의 내용
     */
    private void sendMail(JavaMailSender mailSender, String setFrom, String toMail,
                          String title, String content) {
        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

            helper.setFrom(setFrom);
            helper.setTo(toMail);
            helper.setSubject(title);
            helper.setText(content, true);

            mailSender.send(message);
            log.info("이메일 전송 성공: {}", toMail);
        } catch (MessagingException e) {
            log.error("이메일 전송 실패: {}", e.getMessage(), e);
            // 더 구체적인 오류 메시지 로딩
            if (e.getMessage().contains("535 5.7.1")) {
                throw new ServerProcessingException(ExceptionCode.INVALID_EMAIL_AUTH_CODE);

            } else {
                throw new ServerProcessingException(ExceptionCode.INVALID_EMAIL_AUTH_CODE);
            }
        }
    }

    @Override
    public boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

    /**
     * 인증 번호와 이메일을 Redis에 임시 저장합니다. (유효기간: 3분 30초)
     *
     * @param email 사용자 이메일
     * @param code 인증 코드
     * @return 저장된 인증 코드
     */
    @Transactional
    private String storeAuthCode(final String email, final String code) {
        try {
            String key = AUTH_CODE_PREFIX + email;
            redisUtil.setDataExpire(key, code, AUTH_CODE_EXPIRY_SECONDS);
            log.info("인증 코드 저장 완료: {}={}", key, code);
            return code;
        } catch (Exception e) {
            log.error("Redis 인증 코드 저장 중 오류 발생: {}", e.getMessage(), e);
            throw new ServerProcessingException(ExceptionCode.TOKEN_GENERATION_ERROR);
        }
    }

    @Override
    @Transactional
    public void deleteAuthCode(final String email) {
        try {
            String key = AUTH_CODE_PREFIX + email;
            if (redisUtil.hasKey(key)) {
                redisUtil.deleteData(key);
                log.info("인증 코드 삭제 완료: {}", key);
            }
        } catch (Exception e) {
            log.warn("Redis 인증 코드 삭제 중 오류 발생: {}", e.getMessage(), e);
            throw new ServerProcessingException(ExceptionCode.INVALID_EMAIL_AUTH_CODE);
        }
    }

    @Override
    public String createKey(String email) {
        return AUTH_CODE_PREFIX + email;
    }

    @Override
    public String createCode() throws NoSuchAlgorithmException {
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < AUTH_CODE_LENGTH; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("인증 코드 생성 중 오류 발생: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void checkDuplicatedEmail(String email) {
        Optional<String> findEmail = userRepository.findEmailByEmail(email);
        findEmail.ifPresentOrElse(
                em -> {
                    log.debug("이메일 중복 확인: 이미 존재하는 이메일 {}", em);
                    throw new BadRequestException(ExceptionCode.DUPLICATE_EMAIL_MYSQL);
                },
                () -> log.info("이메일 중복 확인: 사용 가능한 이메일 {}", email)
        );
    }

    @Override
    public AuthCodeVerificationResult verifiedCode(final String email, final String authCode) {
        this.checkDuplicatedEmail(email);

        String key = AUTH_CODE_PREFIX + email;
        String redisAuthCode = redisUtil.getData(key);

        if (redisAuthCode == null) {
            throw new ServerProcessingException(ExceptionCode.TOKEN_GENERATION_ERROR);
        }

        log.info("인증 코드 검증: email={}, code={}, storedCode={}", email, authCode, redisAuthCode);
        boolean authResult = redisAuthCode.equals(authCode);

        return new AuthCodeVerificationResult(authResult);
    }

    @Override
    @Transactional(readOnly = true)
    public void checkDuplication(String email) {
        if (email == null || !this.isValidEmail(email)) {
            throw new BadRequestException(ExceptionCode.INVALID_EMAIL_FORMAT);
        }

        // Redis에서 중복 확인
        checkDuplicationByRedis(email);

        // DB에서 중복 확인
        checkDuplicationByDB(email);
    }

    /**
     * Redis에서 이메일 중복을 확인합니다.
     *
     * @param email 중복 확인할 이메일
     */
    @Transactional(readOnly = true)
    private void checkDuplicationByRedis(@Email String email) {
        if (redisUtil.hasKey(AUTH_CODE_PREFIX + email)) {
            throw new BadRequestException(ExceptionCode.DUPLICATE_EMAIL_REDIS);
        }
    }

    /**
     * DB에서 이메일 중복을 확인합니다.
     *
     * @param email 중복 확인할 이메일
     */
    @Transactional(readOnly = true)
    private void checkDuplicationByDB(@Email String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException(ExceptionCode.DUPLICATE_EMAIL_MYSQL);
        }
    }
}