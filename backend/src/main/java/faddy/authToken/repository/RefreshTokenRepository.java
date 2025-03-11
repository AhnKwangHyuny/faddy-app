package faddy.authToken.repository;

import faddy.authToken.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken , String> {

    Optional<RefreshToken> findById(String id);

}
