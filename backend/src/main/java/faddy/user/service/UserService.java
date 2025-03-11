package faddy.user.service;

import faddy.auth.jwt.Service.JwtUtil;
import faddy.global.Utils.RedisUtil;
import faddy.global.exception.*;
import faddy.user.domain.User;
import faddy.user.dto.request.SignupInfoDto;
import faddy.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;

    private final UserIdEncryptionUtil userIdEncryptionUtil;
    private final PasswordEncoder passwordEncoder;


    public boolean isUserIdDuplicated(final String field, final String value) {

        return switch (field) {
            case "userId" -> userRepository.findUsernameByUsername(value).isPresent();
            case "nickname" -> userRepository.findNicknameByNickname(value).isPresent();
            default -> throw new IllegalArgumentException("Invalid field: " + field);
        };
    }

    @Transactional
    public Optional<String> joinUser(@Valid final SignupInfoDto info) {

        User savedUser = null;

        try {

            User user = new User.Builder()
                    .withUsername(info.getUsername())
                    .withPassword(passwordEncoder.encode(info.getPassword())) // password 암호화 하여 저장
                    .withNickname(info.getNickname())
                    .withEmail(info.getEmail())
                    .build();

            savedUser = userRepository.save(user);


        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage() + ex);
            throw new BadRequestException(ExceptionCode.INVALID_REQUEST);
        }

        if (savedUser == null) {
            throw new InternalServerException(ExceptionCode.NOT_SAVE_USER);
        }

        return Optional.ofNullable(savedUser.getUsername());
    }

    /**
     * @param token 사용자 토큰(JWT)
     * @return 암호화된 사용자 ID, 사용자가 존재하지 않는 경우 null 반환
     * @title: 사용자 토큰(JWT)으로부터 사용자 ID를 가져와 암호화하여 반환합니다.
     */
    @Transactional(readOnly = true)
    public String findEncryptedUserId(String token) {

        String raw_data = jwtUtil.extractRawToken(token);

        String username = jwtUtil.getUsername(raw_data);

        System.out.println("username = " + username);
        if (username == null || username.isEmpty()) {
            return null;
        }

        try {
            Long userId = userRepository.findUserIdByUsername(username);
            return userIdEncryptionUtil.encryptUserId(userId);
        } catch (RuntimeException e) {
            log.warn("do not find userId by username : " + username);
            return null;
        }
    }

    @Transactional(readOnly = true)
    public Long findUserIdByUsername(String username) {
        return userRepository.findUserIdByUsername(username);
    }
    @Transactional(readOnly = true)
    public User getUserById(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.INVALID_USER_ID));
    }

    @Transactional(readOnly = true)
    public User findUserByToken(String token) {

        String rawToken = jwtUtil.extractRawToken(token);

        String username = jwtUtil.getUsername(rawToken);

        return userRepository.findByUsername(username)
                .orElseThrow(()-> {
                    log.warn("[UserService] [findUserByToken] username이 존재하지 않습니다.");
                    throw new BadRequestException(ExceptionCode.INVALID_USER_ID);
                });
    };

    public Boolean checkEncrptedUserIdExists(String encrptedUserId) {

        Long userId = userIdEncryptionUtil.decryptUserId(encrptedUserId);

        if(userId == null) {
            throw new BadRequestException(ExceptionCode.DECRYPT_USER_ID_ERROR);
        }

        return userRepository.existsById(userId);
    }

    public Long getUserIdByEncrptedUserId(String encrptedUserId) {
        Long userId = userIdEncryptionUtil.decryptUserId(encrptedUserId);

        if(userId == null) {
            throw new BadRequestException(ExceptionCode.DECRYPT_USER_ID_ERROR);
        }

        if(userRepository.existsById(userId)) {
            return userId;
        }

        return null;
    }


    @Transactional
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException(
                        HttpStatus.BAD_REQUEST.value(),
                        "해당 유저 아이디가 존재하지 않습니다.")
                );
    }

    public Long decryptUserId(String encryptedUserId) {

        try {
            return userIdEncryptionUtil.decryptUserId(encryptedUserId);

        } catch (Exception e) {

            StackTraceElement[] stackTrace = e.getStackTrace();
            StackTraceElement currentMethod = stackTrace[0]; // 현재 메소드 정보

            String errorMessage = currentMethod.getClassName() + "."
                    + currentMethod.getMethodName()
                    + " userId decoding 중 에러가 발생했습니다.  "
                    + e.getMessage();

            throw new AuthorizationException(
                    HttpStatus.UNAUTHORIZED.value(),
                    ""
            );
        }
    }

    /**
     * 일정 스냅 , talk 포스팅 , 유저 팔로우 , 좋아요 획득 시 레벨 업
     * @param  User : 해당 유저
     * */
//    @Transactional
//    public void updateUserLevel(User user) {
//        int postCount = user.getSnaps().size();
//
//        // level up 조건(추후 개발)
//        user.getProfile().levelUp();
//    }

    /**
     *  authorization token을 통해 userId 가져오기
     * @Param String authorization Token
     * @Return Long userId
     * */
    @Transactional(readOnly = true)
    public Long getUserIdByAuthorization(String token) {
        // 만약 token에 bearer가 있다면 제외
        token = jwtUtil.extractRawToken(token);

        // token에서 username 추출
        String username = jwtUtil.getUsername(token);

        User user = findByUsername(username);

        if(user == null) {
            throw new AuthorizationException(HttpStatus.UNAUTHORIZED.value() ,
                    "[User Service.getUserIdByAuthrization 유저를 찾을 수 없습니다.]");
        }

        return user.getId();
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AuthenticationException(ExceptionCode.INVALID_USER_ID));
    }

    // id로 사용자 닉네임 가져오기
    public String getNicknameById(Long userId) {
        return userRepository.findNicknameByUserId(userId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.INVALID_USER_ID));
    }

    //토큰을 이용해서 사용자 아이디 조회 (username)
    public String getUsernameByToken(String token) {
        String rawToken = jwtUtil.extractRawToken(token);
        return jwtUtil.getUsername(rawToken);
    }

    // 사용자 아이디로 사용자 계정 조회
    public String getUsernameByUserId(Long userId) {
        return userRepository.findUsernameByUserId(userId)
                .orElse(null);
    }

    public List<Long> decryptUserIds(List<String> encryptedUserIds) {
        return encryptedUserIds.stream()
                .map(this::decryptUserId)
                .toList();
    }

    public User getUserWithProfileByUsername(String username) {
        return userRepository.findUserByUsernameWithProfile(username)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.INVALID_USER_ID));
    }

    public User getUserWithProfileById(Long userId) {
        return userRepository.findUserWithProfileByUserId(userId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.INVALID_USER_ID));
    }

    @Transactional(readOnly = true)
    public Long findUserIdByToken(String token) {
        String rawToken = jwtUtil.extractRawToken(token);

        String username = jwtUtil.getUsername(rawToken);

        return userRepository.findUserIdByUsername(username);

    }

    public List<User> findUsersByUserIds(List<Long> userIds) {
        return userRepository.findUsersByUserIds(userIds);
    }
}

