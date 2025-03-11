package faddy.authToken.service;

import faddy.authToken.infrastructure.AuthTokensGenerator;
import faddy.authToken.repository.BlackListTokenRepository;
import faddy.global.exception.AuthorizationException;
import faddy.global.exception.ExceptionCode;
import faddy.global.exception.UnAuthorizationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RefreshTokenValidator {

    private final AuthTokensGenerator authTokensGenerator;
    private final BlackListTokenRepository blackListTokenRepository;

    public void validateToken(String refreshToken) {
        if (!authTokensGenerator.isValidToken(refreshToken)) {
            throw new AuthorizationException(ExceptionCode.INVALID_REFRESH_TOKEN);
        }
    }

    public void validateTokenOwnerUsername(String refreshToken, String username) {
        final String ownerUsername = authTokensGenerator.extractUsername(refreshToken);
        if (!ownerUsername.equals(username)) {
            throw new AuthorizationException(ExceptionCode.TOKEN_OWNER_MISMATCH);
        }
    }

    public void validateLogoutToken(String refreshToken) {
        if (blackListTokenRepository.existsByInvalidRefreshToken(refreshToken)) {
            throw new UnAuthorizationException(ExceptionCode.ALREADY_LOGOUT);
        }
    }
}
