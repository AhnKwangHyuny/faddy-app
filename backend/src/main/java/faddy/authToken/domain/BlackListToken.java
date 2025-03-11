package faddy.authToken.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Getter
@Entity
public class BlackListToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invalid_refresh_token", unique = true) // 중복 방지를 위해 unique 설정
    private String invalidRefreshToken;


    public BlackListToken(String invalidRefreshToken) {
        this.invalidRefreshToken = invalidRefreshToken;
    }
}
