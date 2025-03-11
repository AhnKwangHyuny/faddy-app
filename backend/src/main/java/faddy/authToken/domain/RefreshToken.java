package faddy.authToken.domain;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash(value = "refresh_token" , timeToLive =604800 )
@AllArgsConstructor
@Getter
public class RefreshToken {

    @Id
    private String id;

    private String refreshToken;

}
