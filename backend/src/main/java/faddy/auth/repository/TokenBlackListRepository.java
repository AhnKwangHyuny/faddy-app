package faddy.auth.repository;

import faddy.auth.domain.TokenBlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenBlackListRepository extends JpaRepository<TokenBlackList , Long> {

    Optional<TokenBlackList> findByInvalidRefreshToken(String refreshToken);

    default boolean existsByRefreshToken(String refreshToken) {
        return findByInvalidRefreshToken(refreshToken).isPresent();
    }

}
