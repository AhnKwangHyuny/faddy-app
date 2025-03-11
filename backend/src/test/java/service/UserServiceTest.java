package service;

import faddy.auth.jwt.Service.JwtUtil;
import faddy.global.Utils.RedisUtil;
import faddy.user.domain.User;
import faddy.user.dto.request.SignupInfoDto;
import faddy.user.repository.UserRepository;
import faddy.user.service.UserIdEncryptionUtil;
import faddy.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * UserService 단위 테스트
 * 사용자 관련 서비스 로직을 테스트합니다.
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RedisUtil redisUtil;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserIdEncryptionUtil userIdEncryptionUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("회원가입 성공 테스트")
    void joinUserSuccess() {
        // given
        SignupInfoDto signupInfoDto = new SignupInfoDto();
        signupInfoDto.setUsername("newuser");
        signupInfoDto.setPassword("Password123!");
        signupInfoDto.setNickname("신규유저");
        signupInfoDto.setEmail("new@example.com");

        User mockUser = new User.Builder()
                .withUsername("newuser")
                .withPassword("encodedPassword")
                .withNickname("신규유저")
                .withEmail("new@example.com")
                .withAuthority("USER")
                .build();

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // when
        Optional<String> result = userService.joinUser(signupInfoDto);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo("newuser");
        verify(passwordEncoder).encode("Password123!");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("사용자명 중복 확인 테스트")
    void checkUsernameDuplication() {
        // given
        String username = "existinguser";
        when(userRepository.findUsernameByUsername(username)).thenReturn(Optional.of(username));

        // when & then
        assertThat(userService.isUserIdDuplicated("userId", username)).isTrue();
        verify(userRepository).findUsernameByUsername(username);
    }

    @Test
    @DisplayName("닉네임 중복 확인 테스트")
    void checkNicknameDuplication() {
        // given
        String nickname = "기존닉네임";
        when(userRepository.findNicknameByNickname(nickname)).thenReturn(Optional.of(nickname));

        // when & then
        assertThat(userService.isUserIdDuplicated("nickname", nickname)).isTrue();
        verify(userRepository).findNicknameByNickname(nickname);
    }

    @Test
    @DisplayName("잘못된 필드명으로 중복 확인 시 예외 발생 테스트")
    void checkDuplicationWithInvalidField() {
        // given
        String invalidField = "invalidField";
        String value = "testValue";

        // when & then
        assertThatThrownBy(() -> userService.isUserIdDuplicated(invalidField, value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid field");
    }

    @Test
    @DisplayName("토큰으로 사용자 ID 조회 테스트")
    void findUserIdByToken() {
        // given
        String token = "Bearer test-token";
        String rawToken = "test-token";
        String username = "tokenuser";
        Long userId = 123L;

        given(jwtUtil.extractRawToken(token)).willReturn(rawToken);
        given(jwtUtil.getUsername(rawToken)).willReturn(username);
        given(userRepository.findUserIdByUsername(username)).willReturn(userId);

        // when
        Long result = userService.findUserIdByToken(token);

        // then
        assertThat(result).isEqualTo(userId);
        verify(jwtUtil).extractRawToken(token);
        verify(jwtUtil).getUsername(rawToken);
        verify(userRepository).findUserIdByUsername(username);
    }
}