package faddy.email.service;

import faddy.email.dto.AuthCodeMessage;
import faddy.global.Utils.RedisUtil;
import faddy.api.response.AuthCodeVerificationResult;
import faddy.global.exception.BadRequestException;
import faddy.global.exception.ExceptionCode;
import faddy.global.exception.ServerProcessingException;
import faddy.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Description;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  이메일 인증번호 전송 서비스를 제공
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;
    private final UserRepository userRepository;

    private static final String AUTH_CODE_PREFIX = "AuthCode";


    /**
     * 사용자가 입력한 인증번호와 실제 인증 번호를 비교한다
     */
    public boolean CheckAuthNum(String email,String authNum){
        if(redisUtil.getData(authNum)==null){
            return false;
        }
        else if(redisUtil.getData(authNum).equals(email)){
            return true;
        }
        else{
            return false;
        }
    }


    /**
     * 회원 가입 인증 이메일을 생성하고 전송합니다.
     *
     * @param email 인증 이메일을 받을 사용자의 이메일 주소
     * @return 생성된 인증 번호
     */
    @Transactional
    public String sendCodeToMail(String email) throws NoSuchAlgorithmException {

        String authCode = this.createCode();

        // mail contents
        AuthCodeMessage content = AuthCodeMessage.createMessage(email, authCode);

        try {
            // 요청자 이메일에 인증번호 발송
            this.sendMail(content.getSetFrom(), content.getToMail(), content.getTitle(), content.getContent());


            //mail 전송 후 redis에 인증 토큰 저장
            this.storeAuthCode(email , authCode);



        } catch (Exception e){
//            throw new

        }

        return authCode;
    }

    /**
     * 이메일을 전송합니다.
     *
     * @param setFrom 발신자의 이메일 주소
     * @param toMail 수신자의 이메일 주소
     * @param title 이메일의 제목
     * @param content 이메일의 내용
     */
    @Transactional
    private void sendMail(String setFrom, String toMail, String title, String content) {

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");
            helper.setFrom(setFrom);
            helper.setTo(toMail);
            helper.setSubject(title);
            helper.setText(content,true);
            mailSender.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 이메일 형식인지 확인
     *
     * @param email
     * @return boolean
     */

    public boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

    /**
    *  인증 번호와 이메일 redis에 임시 저장 (유효기간 : 3분 5초)
    *  @Param authCode : 인증 번호
    * @Param email : 인증 이메일
     * @Return String code
    * */

    @Transactional
    private String storeAuthCode(final String email ,final String code) {
        try{
            String key = AUTH_CODE_PREFIX + email;

            redisUtil.setDataExpire(key, code ,70*3L); // {key,value} 3분동안 저장.

            log.info("before verified authCode : " + redisUtil.getData(key) );
            return  code;

        }catch (Exception exception){
            exception.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    private void deleteAuthCode(final String email ,final String code) {
        try{
            String key = AUTH_CODE_PREFIX + email;

            if(redisUtil.hasKey(key)) {
                redisUtil.deleteData(key);
            }

        }catch (Exception e){

            log.warn("redis 인증 코드 삭제 중 어떠한 이유로 오류가 발생함. ");

            throw e;

        }
    }

    @Description("레디스 인증번호 value key 생성")
    public String createKey(String email){
        return AUTH_CODE_PREFIX + email;
    }

    /**
     * 임의의 6자리 양수 code를 생성한다.
     */
    public String createCode() throws NoSuchAlgorithmException {

        final int length = 6;

        try {
            Random random = SecureRandom.getInstanceStrong();

            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();

        } catch (NoSuchAlgorithmException e) {
            log.debug("MemberService.createCode() exception occur");
            throw new NoSuchAlgorithmException();
        }
    }

    /**
     * 추후 emailVerification entity 생성 시 로직 email 중복확인 쿼리 달라질 거임
     * */

    public void checkDuplicatedEmail(String email) {

        Optional<String> findEmail = userRepository.findEmailByEmail(email);

        findEmail.ifPresentOrElse(
                em -> {
                    // 중복 이메일 존재할 경우
                    log.debug("MemberServiceImpl.checkDuplicatedEmail exception occur email: {}", em);
                    throw new BadRequestException(ExceptionCode.DUPLICATE_EMAIL_MYSQL);
                },
                () -> {
                    // 중복 이메일 존재 하지 않음
                    log.info("email : ");
                }
        );


    }

    /**
     * @Param 인증 코드 email
     * @Param 인증 번호
     *
     * @Return 인증 코드가 유효한 코드이며 인증 요청 시 이메일로 발송된 코드와 일치하는지 확인 후 result 객체 반환
     * */
    public AuthCodeVerificationResult verifiedCode(final String email , final String authCode) {
        this.checkDuplicatedEmail(email);

        String key = AUTH_CODE_PREFIX + email;

        String redisAuthCode = redisUtil.getData(key);

        // redis key가 null이면 throw ServerError
        if (redisAuthCode == null) {
            throw new ServerProcessingException(ExceptionCode.TOKEN_GENERATION_ERROR);
        }

        log.info("email : " + email + " code : " + authCode + "  redisAuthCode : " + redisAuthCode);

        boolean authResult = redisAuthCode != null && redisAuthCode.equals(authCode);


        return new AuthCodeVerificationResult(authResult);
    }

    /**
     * @Param 중복 확인할 이메일
     *
     * @Return 이메일 중복 확인 시 : true , 중복된 이메일리 존재하지 않을 시 false 리턴
     * */

    @Transactional(readOnly = true)
    public void checkDuplication(String email) {

        if(email == null || !this.isValidEmail(email) ) {
            throw new BadRequestException(ExceptionCode.INVALID_EMAIL_FORMAT);
        }

        // redis에서 중복확인
        checkDuplicationByRedis(email);

        // mysql에서 중복 f
        checkDuplicationByDB(email);

    }

    @Transactional(readOnly = true)
    private void checkDuplicationByRedis(@Email String email) {

        if (redisUtil.hasKey(AUTH_CODE_PREFIX + email)) {
            throw new BadRequestException(ExceptionCode.DUPLICATE_EMAIL_REDIS );
        }

    }

    @Transactional(readOnly = true)
    private void checkDuplicationByDB(@Email String email) {

        if(userRepository.existsByEmail(email)) {
            throw new BadRequestException(ExceptionCode.DUPLICATE_EMAIL_MYSQL );
        }

    }
}
