package faddy.email.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
     * gmail , naver SMTP 서버를 위한 JavaMailSender 객체를 생성하고 반환합니다.
     *
     * @return 설정된 속성을 가진 JavaMailSender 객체
     */

    @Bean
    @Primary
    public JavaMailSender gmailMailSender() {
        return createMailSender(gmailHost, gmailPort , gmailUsername , gmailPassword);
    }

    @Bean
    public JavaMailSender naverMailSender() {

        return createMailSender(naverHost, naverPort , naverUsername , naverPassword);
    }


    /**
     * 주어진 설정을 사용하여 JavaMailSender 객체를 생성합니다.
     *
     * @param host SMTP 서버의 호스트 이름
     * @param port SMTP 서버의 포트 번호
     * @param username SMTP 서버의 사용자 이름
     * @param password SMTP 서버의 비밀번호
     * @return 설정된 속성을 가진 JavaMailSender 객체
     */

    private JavaMailSender createMailSender(String host, int port, String username, String password) {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        //  JavaMail의 속성을 설정하기 위한 Properties 객체를 생성

        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.transport.protocol", "smtp");
        javaMailProperties.put("mail.smtp.auth", "true");
        javaMailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); // SSL 소켓 팩토리 클래스를 사용
        javaMailProperties.put("mail.smtp.starttls.enable", "true"); // STARTTLS를 사용하여 암호화된 통신을 활성화
        javaMailProperties.put("mail.debug", "true"); // 디버깅 정보를 출력
        javaMailProperties.put("mail.smtp.ssl.trust", host);
        javaMailProperties.put("mail.smtp.ssl.protocols", "TLSv1.2");

        mailSender.setJavaMailProperties(javaMailProperties); // JavaMailSender에 커스텀된 설정 사용

        return mailSender;
    }
}
