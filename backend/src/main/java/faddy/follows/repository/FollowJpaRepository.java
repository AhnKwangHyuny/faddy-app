package faddy.follows.repository;

import faddy.follows.domain.Follow;
import faddy.user.domain.User;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowJpaRepository extends JpaRepository<Follow, Long> {

    // 팔로우 요청한 유저 간 이미 팔로우 상태인지 확인하는 쿼리
    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END " +
            "FROM Follow f " +
            "WHERE f.following = :following AND f.follower = :follower")
    boolean existsByFollowingAndFollower(@Param("following") User following, @Param("follower") User follower);

    // 언팔

    Optional<Follow> deleteByFollowingAndFollower( User following, User follower);

    //following 조회

    // following을 기준으로 팔로워 목록 조회
    @Query("SELECT f FROM Follow f JOIN FETCH f.follower JOIN FETCH f.follower.profile WHERE f.following.id = :userId")
    List<Follow> findAllByFollowingId(@Param("userId") Long userId);

    // follower를 기준으로 팔로잉 목록 조회
    @Query("SELECT f FROM Follow f JOIN FETCH f.following JOIN FETCH f.following.profile WHERE f.follower.id = :userId")
    List<Follow> findAllByFollowerId(@Param("userId") Long userId);


}
