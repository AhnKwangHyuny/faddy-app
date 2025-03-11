package faddy.auth.jwt.Service;

import faddy.auth.domain.TokenBlackList;
import faddy.auth.repository.TokenBlackListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class TokenBlackListService {

    private final TokenBlackListRepository tokenBlackListRepository;

    public TokenBlackListService(TokenBlackListRepository tokenBlackListRepository) {
        this.tokenBlackListRepository = tokenBlackListRepository;
    }

    /**
     * 주어진 리프레시 토큰을 블랙리스트에 추가합니다.
     * @param refreshToken 블랙리스트에 추가할 리프레시 토큰
     * @return 저장된 TokenBlackList 엔티티
     */
    public TokenBlackList blacklistRefreshToken(String refreshToken) {
        TokenBlackList tokenBlackList = new TokenBlackList(refreshToken);
        return tokenBlackListRepository.save(tokenBlackList);
    }

    /**
     * 주어진 리프레시 토큰이 블랙리스트에 있는지 확인합니다.
     * @param refreshToken 확인할 리프레시 토큰
     * @return 블랙리스트에 있다면 true, 아니면 false
     */
    public boolean isBlacklisted(String refreshToken) {
        Optional<TokenBlackList> tokenBlackList = tokenBlackListRepository.findByInvalidRefreshToken(refreshToken);
        return tokenBlackList.isPresent();
    }

    /**
     * 주어진 리프레시 토큰을 블랙리스트에서 제거합니다.
     * @param refreshToken 블랙리스트에서 제거할 리프레시 토큰
     */
    public void removeFromBlacklist(String refreshToken) {
        tokenBlackListRepository.findByInvalidRefreshToken(refreshToken)
                .ifPresent(tokenBlackListRepository::delete);
    }
}
