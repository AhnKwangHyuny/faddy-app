package faddy.authToken.infrastructure;

import faddy.auth.api.response.AuthTokensResponse;
import faddy.auth.jwt.Service.JwtUtil;
import faddy.global.exception.ExceptionCode;
import faddy.global.exception.JwtValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@RequiredArgsConstructor
@Component
public class AuthTokensGenerator {
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 5;	   // 5시간
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일

    private final JwtUtil jwtUtil;

    @Transactional
    public AuthTokensResponse generate(String username) {
        // 유저네임 null 체크


        if (username == null) {
            throw new JwtValidationException(ExceptionCode.ERROR_TOKEN_CREATE);
        }

        long now = System.currentTimeMillis();

        Date accessTokenExpiredAt = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpiredAt = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        String accessToken;
        String refreshToken;

        // 토큰 생성
        try {
            accessToken = jwtUtil.generate(username, accessTokenExpiredAt);
            refreshToken = jwtUtil.generate(username, refreshTokenExpiredAt);
        } catch (RuntimeException e) {
            throw new JwtValidationException(ExceptionCode.ERROR_TOKEN_CREATE);
        }


        // 리프레시 토큰 저장
        jwtUtil.storeRefreshToken(username, refreshToken);

        // 응답 생성 및 반환
        return AuthTokensResponse.of(accessToken, refreshToken, BEARER_TYPE, ACCESS_TOKEN_EXPIRE_TIME / 1000L);
    }


    public boolean isValidToken(String token) {
        return jwtUtil.validateToken(token);
    }

    public String extractUsername(String accessToken) {
        return jwtUtil.getUsername(accessToken);
    }
}