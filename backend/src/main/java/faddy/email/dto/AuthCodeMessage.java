package faddy.email.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthCodeMessage {
    private String setFrom;
    private String toMail;
    private String title;
    private String content;

    public static AuthCodeMessage createMessage(String email, String authNumber) {
        String setFrom = email;
        String toMail = email;
        String title = "회원 가입 인증 이메일 입니다.";
        String content =
                "나의 APP을 방문해주셔서 감사합니다." +
                        "<br><br>" +
                        "인증 번호는 " + authNumber + "입니다." +
                        "<br>" +
                        "인증번호를 3분 이내로 제대로 입력해주세요";

        return AuthCodeMessage.builder()
                .setFrom(setFrom)
                .toMail(toMail)
                .title(title)
                .content(content)
                .build();
    }
}
