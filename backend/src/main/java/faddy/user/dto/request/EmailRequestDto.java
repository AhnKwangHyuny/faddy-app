package faddy.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class EmailRequestDto {

    /*
     * 1) 이메일 주소는 @ 기호를 포함해야 합니다.
     * 2) @ 기호를 기준으로 로컬 호스트 부분과 도메인 부분이 존재해야 합니다.
     * 3) 도메인 부분은 최소한 하나의 점(.)을 포함해야 합니다.
     * 4) gmail 과 naver 도메인만을 허용함
     */
    @Email(regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:gmail.com|naver.com)$")
    @NotEmpty(message = "이메일을 입력해 주세요")
    private String email;

    public String getEmail() {
        return this.email;
    }
}
