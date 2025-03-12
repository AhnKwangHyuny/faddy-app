package faddy.email.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * 이 클래스는 이메일 서비스를 위한 설정을 제공한다.
 * Gmail과 Naver SMTP 서버를 위한 JavaMailSender bean 객체를 생성하고 반환한다.
 */

@Configuration
public class EmailConfig {

    @Value("${gmail.host}")
    private String gmailHost;

    @Value("${gmail.port}")
    private int gmailPort;

    @Value("${gmail.username}")
    private String gmailUsername;

    @Value("${gmail.password}")
    private String gmailPassword;

    @Value("${naver.host}")
    private String naverHost;

    @Value("${naver.port}")
    private int naverPort;

    @Value("${naver.username}")
    private String naverUsername;

    @Value("${naver.password}")
    private String naverPassword;

    /**
     * Gmail 이메일 인증을 위한 MailSender 생성
     *
     * @return Gmail MailSender
     */
    @Bean(name = "gmailSender")
    public JavaMailSender gmailMailSender() {
        // Gmail은 앱 비밀번호 또는 OAuth2 인증이 필요합니다
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(gmailHost);
        mailSender.setPort(gmailPort);
        mailSender.setUsername(gmailUsername);
        mailSender.setPassword(gmailPassword);

        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.ssl.trust", gmailHost);
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        mailSender.setJavaMailProperties(props);
        return mailSender;
    }

    /**
     * Naver 이메일 인증을 위한 MailSender 생성
     *
     * @return Naver MailSender
     */
    @Bean(name = "naverSender")
    public JavaMailSender naverMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(naverHost);
        mailSender.setPort(naverPort);
        mailSender.setUsername(naverUsername);
        mailSender.setPassword(naverPassword);

        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.ssl.trust", naverHost);
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        mailSender.setJavaMailProperties(props);
        return mailSender;
    }

}
