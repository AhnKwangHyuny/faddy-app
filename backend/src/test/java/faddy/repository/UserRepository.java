package faddy.repository;

import faddy.user.domain.Authority;
import faddy.user.domain.User;
import faddy.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * User 엔티티의 JPA 연동 테스트 (MySQL)
 * 실제 MySQL 데이터베이스를 사용하여 User 엔티티 CRUD 기능을 테스트합니다.
 */
@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 실제 DB 사용
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("User 엔티티 저장 테스트")
    void saveUser() {
        // given
        User user = new User.Builder()
                .withUsername("testuser")
                .withPassword("Password123!")
                .withNickname("테스트유저")
                .withEmail("test@example.com")
                .withAuthority("USER")
                .build();

        // when
        User savedUser = userRepository.save(user);
        entityManager.flush();
        entityManager.clear();

        // then
        User foundUser = userRepository.findById(savedUser.getId()).orElse(null);
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("testuser");
        assertThat(foundUser.getNickname()).isEqualTo("테스트유저");
        assertThat(foundUser.getEmail()).isEqualTo("test@example.com");
        assertThat(foundUser.getAuthority()).isEqualTo(Authority.ROLE_USER);
    }

    @Test
    @DisplayName("사용자명으로 User 조회 테스트")
    void findUserByUsername() {
        // given
        User user = new User.Builder()
                .withUsername("finduser")
                .withPassword("Password123!")
                .withNickname("찾을유저")
                .withEmail("find@example.com")
                .withAuthority("USER")
                .build();

        entityManager.persist(user);
        entityManager.flush();
        entityManager.clear();

        // when
        Optional<User> foundUser = userRepository.findByUsername("finduser");

        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("finduser");
        assertThat(foundUser.get().getNickname()).isEqualTo("찾을유저");
    }

    @Test
    @DisplayName("사용자명 중복 확인 테스트")
    void checkUsernameDuplication() {
        // given
        User user = new User.Builder()
                .withUsername("duplicated")
                .withPassword("Password123!")
                .withNickname("중복유저")
                .withEmail("duplicate@example.com")
                .withAuthority("USER")
                .build();

        entityManager.persist(user);
        entityManager.flush();

        // when
        Optional<String> username = userRepository.findUsernameByUsername("duplicated");

        // then
        assertThat(username).isPresent();
        assertThat(username.get()).isEqualTo("duplicated");
    }

    @Test
    @DisplayName("닉네임 중복 확인 테스트")
    void checkNicknameDuplication() {
        // given
        User user = new User.Builder()
                .withUsername("nickname_test")
                .withPassword("Password123!")
                .withNickname("중복닉네임")
                .withEmail("nickname@example.com")
                .withAuthority("USER")
                .build();

        entityManager.persist(user);
        entityManager.flush();

        // when
        Optional<String> nickname = userRepository.findNicknameByNickname("중복닉네임");

        // then
        assertThat(nickname).isPresent();
        assertThat(nickname.get()).isEqualTo("중복닉네임");
    }

    @Test
    @DisplayName("여러 사용자 ID로 사용자 목록 조회 테스트")
    void findUsersByIds() {
        // given
        User user1 = new User.Builder()
                .withUsername("user1")
                .withPassword("Password123!")
                .withNickname("유저1")
                .withEmail("user1@example.com")
                .withAuthority("USER")
                .build();

        User user2 = new User.Builder()
                .withUsername("user2")
                .withPassword("Password123!")
                .withNickname("유저2")
                .withEmail("user2@example.com")
                .withAuthority("USER")
                .build();

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();
        entityManager.clear();

        // when
        List<User> users = userRepository.findByUserIds(List.of(user1.getId(), user2.getId()));

        // then
        assertThat(users).hasSize(2);
        assertThat(users).extracting("username").containsExactlyInAnyOrder("user1", "user2");
    }

    @Test
    @DisplayName("사용자 ID로 닉네임 조회 테스트")
    void findNicknameByUserId() {
        // given
        User user = new User.Builder()
                .withUsername("nickname_lookup")
                .withPassword("Password123!")
                .withNickname("조회닉네임")
                .withEmail("lookup@example.com")
                .withAuthority("USER")
                .build();

        entityManager.persist(user);
        entityManager.flush();

        // when
        Optional<String> nickname = userRepository.findNicknameByUserId(user.getId());

        // then
        assertThat(nickname).isPresent();
        assertThat(nickname.get()).isEqualTo("조회닉네임");
    }

    @Test
    @DisplayName("사용자 논리적 삭제 테스트")
    void deleteUserLogically() {
        // given
        User user = new User.Builder()
                .withUsername("delete_test")
                .withPassword("Password123!")
                .withNickname("삭제테스트")
                .withEmail("delete@example.com")
                .withAuthority("USER")
                .build();

        User savedUser = entityManager.persist(user);
        entityManager.flush();

        // when
        userRepository.deleteByUserId(savedUser.getId());
        entityManager.flush();

        // then - 논리적 삭제이므로 findById로는 여전히 찾을 수 있어야 함
        assertThat(userRepository.existsById(savedUser.getId())).isTrue();
    }
}