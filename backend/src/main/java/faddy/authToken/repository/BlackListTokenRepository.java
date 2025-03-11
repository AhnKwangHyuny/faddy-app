package faddy.authToken.repository;

import faddy.authToken.domain.BlackListToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlackListTokenRepository extends JpaRepository<BlackListToken, Long> {

    default boolean existsByInvalidRefreshToken(String refreshToken) {
        return findByInvalidRefreshToken(refreshToken).isPresent();
    }

    Optional<BlackListToken> findByInvalidRefreshToken(String refreshToken);
}