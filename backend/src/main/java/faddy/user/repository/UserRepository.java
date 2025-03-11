package faddy.user.repository;

import com.querydsl.core.annotations.QueryEmbeddable;
import faddy.user.domain.Profile;
import faddy.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 주어진 ID로 사용자를 조회
     *
     * @param id 조회할 사용자의 고유 식별자
     * @return 해당 ID를 가진 사용자 (없으면 빈 Optional)
     */
    Optional<User> findById(Long id);

    /**
     * 주어진 사용자 이름으로 사용자를 찾는 메서드
     *
     * @param username 찾고자 하는 사용자의 이름
     * @return 이름이 일치하는 사용자를 Optional로 감싼 객체. 사용자가 없으면 Optional.empty()를 반환
     */
    Optional<User> findByUsername(String username);

    /**
     * 주어진 사용자 이름에 해당하는 사용자 이름만 조회합니다.
     * (사용자 이름 중복 확인 등에 사용)
     *
     * @param username 조회할 사용자 이름
     * @return 일치하는 사용자 이름 (없으면 빈 Optional)
     */
    @Query("SELECT u.username FROM User u WHERE u.username = ?1")
    Optional<String> findUsernameByUsername(String username);

    /**
     * 주어진 닉네임에 해당하는 닉네임만 조회합니다.
     * (닉네임 중복 확인 등에 사용)
     *
     * @param nickname 조회할 닉네임
     * @return 일치하는 닉네임 (없으면 빈 Optional)
     */
    @Query("SELECT u.nickname FROM User u WHERE u.nickname = ?1")
    Optional<String> findNicknameByNickname(String nickname);

    /**
     * 주어진 이메일에 해당하는 이메일만 조회합니다.
     * (이메일 중복 확인 등에 사용)
     *
     * @param email 조회할 이메일
     * @return 일치하는 이메일 (없으면 빈 Optional)
     */
    @Query("SELECT u.email FROM User u WHERE u.email = ?1")
    Optional<String> findEmailByEmail(String email);

    /**
     * 주어진 이메일이 이미 존재하는지 확인합니다.
     *
     * @param email 확인할 이메일
     * @return 이메일이 존재하면 true, 없으면 false
     */
    boolean existsByEmail(String email);

    /**
     * 주어진 사용자 ID가 존재하는지 확인합니다.
     *
     * @param userId 확인할 사용자 ID
     * @return 사용자 ID가 존재하면 true, 없으면 false
     */
    boolean existsById(Long userId);

    /**
     * 주어진 사용자 ID를 가진 사용자를 논리적으로 삭제합니다.
     * (실제 삭제 대신 상태를 'DELETED'로 변경)
     *
     * @param userId 삭제할 사용자의 ID
     */
    @Modifying
    @Query(
            """
            UPDATE User user SET user.status = 'DELETED' WHERE user.id = :userId
            """
    )
    void deleteByUserId(@Param("userId") final Long userId);

    /**
     * 사용자 이름으로 해당 사용자의 ID를 조회합니다.
     *
     * @param username 조회할 사용자 이름
     * @return 해당 사용자의 ID
     */
    @Query("SELECT u.id FROM User u WHERE u.username = :username")
    Long findUserIdByUsername(@Param("username") String username);

    /**
     * 여러 사용자 ID 목록으로 해당하는 모든 사용자 엔티티를 조회합니다.
     *
     * @param userIds 조회할 사용자 ID 목록
     * @return 사용자 ID 목록에 해당하는 사용자 엔티티 목록
     */
    @Query("SELECT u FROM User u WHERE u.id IN (:userIds)")
    List<User> findByUserIds(@Param("userIds") List<Long> userIds);

    /**
     * 사용자 ID로 해당 사용자의 닉네임을 조회합니다.
     *
     * @param userId 조회할 사용자의 ID
     * @return 해당 사용자의 닉네임 (없으면 빈 Optional)
     */
    @Query("SELECT u.nickname FROM User u WHERE u.id = :userId")
    Optional<String> findNicknameByUserId(@Param("userId") Long userId);

    /**
     * 사용자 ID로 해당 사용자의 이름(username)을 조회합니다.
     *
     * @param userId 조회할 사용자의 ID
     * @return 해당 사용자의 이름 (없으면 빈 Optional)
     */
    @Query("SELECT u.username FROM User u WHERE u.id = :userId")
    Optional<String> findUsernameByUserId(@Param("userId") Long userId);

    /**
     * 여러 사용자 ID 목록으로 해당하는 모든 사용자의 닉네임 목록을 조회합니다.
     *
     * @param userIds 조회할 사용자 ID 목록
     * @return 사용자 ID 목록에 해당하는 닉네임 목록
     */
    @Query("SELECT u.nickname FROM User u WHERE u.id IN (:userIds)")
    List<String> findNicknamesByUserIds(@Param("userIds") List<Long> userIds);

    /**
     * 사용자 ID로 해당 사용자의 프로필 정보만 조회합니다.
     *
     * @param userId 조회할 사용자의 ID
     * @return 해당 사용자의 프로필 (없으면 빈 Optional)
     */
    @Query("SELECT u.profile FROM User u WHERE u.id = :userId")
    Optional<Profile> findProfileById(@Param("userId") Long userId);

    /**
     * 사용자 이름으로 프로필 정보가 함께 즉시 로딩된 사용자를 조회합니다.
     * (N+1 문제 방지를 위한 JOIN FETCH 사용)
     *
     * @param username 조회할 사용자 이름
     * @return 프로필 정보가 포함된 사용자 (없으면 빈 Optional)
     */
    @Query("SELECT u FROM User u JOIN FETCH u.profile WHERE u.username = :username")
    Optional<User> findUserByUsernameWithProfile(@Param("username") String username);

    /**
     * 여러 사용자 ID 목록으로 프로필 정보가 함께 즉시 로딩된 사용자 목록을 조회합니다.
     * (채팅방 등에서 여러 사용자 정보와 프로필을 함께 표시할 때 사용)
     *
     * @param userIds 조회할 사용자 ID 목록
     * @return 프로필 정보가 포함된 사용자 목록
     */
    @Query("SELECT u FROM User u JOIN FETCH u.profile WHERE u.id IN (:userIds)")
    List<User> findUsersWithProfileByUserIds(@Param("userIds") List<Long> userIds);

    /**
     * 여러 사용자 ID 목록으로 해당하는 사용자 엔티티 목록을 조회합니다.
     * (프로필 정보는 포함되지 않음)
     *
     * @param userIds 조회할 사용자 ID 목록
     * @return 사용자 ID 목록에 해당하는 사용자 엔티티 목록
     */
    @Query("SELECT u FROM User u WHERE u.id IN (:userIds)")
    List<User> findUsersByUserIds(@Param("userIds") List<Long> userIds);

    /**
     * 사용자 ID로 프로필 정보가 함께 즉시 로딩된 사용자를 조회합니다.
     * (N+1 문제 방지를 위한 JOIN FETCH 사용)
     *
     * @param userId 조회할 사용자의 ID
     * @return 프로필 정보가 포함된 사용자 (없으면 빈 Optional)
     */
    @Query("SELECT u FROM User u JOIN FETCH u.profile WHERE u.id = :userId")
    Optional<User> findUserWithProfileByUserId(@Param("userId") Long userId);
}