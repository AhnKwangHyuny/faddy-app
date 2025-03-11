package faddy.user.presentation;

import faddy.api.Dto.ResponseDto;
import faddy.auth.dto.LoginRequestDto;
import faddy.email.service.MailService;
import faddy.global.Utils.UserValidator;
import faddy.global.exception.BadRequestException;
import faddy.global.exception.ExceptionCode;
import faddy.global.exception.ExceptionResponse;
import faddy.user.dto.request.SignupInfoDto;
import faddy.user.dto.response.UserIdDto;
import faddy.user.repository.UserRepository;
import faddy.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * 사용자 관련 API를 처리하는 컨트롤러
 * 회원가입, 로그인, 중복확인 등의 기능 제공
 */

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final MailService mailService;

    public UserController(UserRepository userRepository, UserService userService, MailService mailService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
    }

    /**
     * BadRequest 예외 처리를 위한 핸들러
     * 클라이언트 오류에 대한 일관된 응답 형식 제공
     *
     * @param e 발생한 BadRequestException 인스턴스
     * @return 오류 메시지와 코드를 포함한 응답 객체
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequestException(final BadRequestException e) {
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(e.getCode(), e.getMessage()));
    }

    /**
     * 유저 회원가입 처리
     *
     * 유저 회원가입 정보 유효성 검사를 진행한 후 DB에 저장합니다.
     * 성공 시 클라이언트 확인용으로 유저 username을 반환합니다.
     *
     * @param joinInfo 회원가입 정보 DTO
     * @return 회원가입 처리 결과와 사용자명
     */
    @PostMapping
    public ResponseEntity<ResponseDto> join(@RequestBody SignupInfoDto joinInfo) {
        Optional<String> username = userService.joinUser(joinInfo);

        Boolean isValid = username
                .filter(value -> value.equals(joinInfo.getUsername()))
                .isPresent();

        if (Boolean.TRUE.equals(isValid)) {
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ResponseDto.response(
                            "201",
                            "환영합니다! 회원가입이 성공적으로 진행되었습니다.",
                            Collections.singletonMap("username", username.get())
                    )
            );
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ResponseDto.response(
                        "500",
                        "죄송합니다. 예상치 못한 오류로 회원가입에 실패했습니다. 다시 가입 부탁드립니다."
                )
        );
    }

    /**
     * 유저 정보 중복 확인
     *
     * 회원가입 시 입력된 유저 정보(아이디, 이메일, 닉네임 등)의 중복 여부를 확인합니다.
     *
     * @param field 확인할 필드명 (username, email, nickname 등)
     * @param user 확인할 값을 포함하는 맵
     * @return 중복 여부 확인 결과
     * @throws BadRequestException 입력 데이터가 유효하지 않거나 중복된 경우
     */
    @PostMapping("/check-duplication/{field}")
    public ResponseEntity<ResponseDto> checkDuplication(@PathVariable("field") @Valid String field, @RequestBody Map<String, String> user) {
        String value = user.get(field);

        // 사용자 아이디가 없거나 null인 경우 HTTP 코드 400 반환 (request error)
        if (value == null || value.isEmpty()) {
            throw new BadRequestException(ExceptionCode.INVALID_INPUT_DATA);
        }

        if (userService.isUserIdDuplicated(field, value)) {
            throw new BadRequestException(ExceptionCode.DUPLICATED_USER_ID);
        }

        return ResponseEntity.ok().body(
                ResponseDto.response(
                        "200",
                        "사용 가능한 " + field + " 입니다."
                )
        );
    }

    /**
     * 유저 로그인 유효성 검사 및 처리
     *
     * 로그인 요청 시 아이디와 패스워드를 확인하고 토큰을 생성하여 반환
     * 로그인 정보 유효성 검사 후 인증 실패 시 에러 응답을 반환
     *
     * @param loginInfo 로그인 요청 정보 DTO
     * @return 로그인 처리 결과 (성공 시 토큰 정보 포함)
     */
    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@Valid @RequestBody LoginRequestDto loginInfo) {
        String username = loginInfo.getUsername();
        String password = loginInfo.getPassword();

        // username password 값 유효성 검사
        if (!UserValidator.isValidUsername(username) || !UserValidator.isValidPassword(password)) {
            // 유효성 검사 실패 시 bad Request 요청
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ResponseDto.response(
                            "400",
                            "잘못된 로그인 정보입니다. 다시 입력바랍니다."
                    )
            );
        }

        return null; // 실제 구현 필요
    }

    /**
     * 클라이언트에 사용자 엔티티 ID 전송
     *
     * 성공적으로 로그인 후 authContext에 저장된 사용자 ID를 암호화하여 클라이언트에 전달합니다.
     *
     * @param request HTTP 요청 객체 (인증 토큰 포함)
     * @return 암호화된 사용자 ID
     */
    @GetMapping("/userId")
    public ResponseEntity<ResponseDto<String>> sendEncryptedUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String encryptedUserId = userService.findEncryptedUserId(token);

        if (encryptedUserId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseDto.response("404", "사용자를 찾을 수 없습니다."));
        }

        return ResponseEntity.ok(ResponseDto.response("200", "사용자 ID 전송 성공", encryptedUserId));
    }

    /**
     * 특정 사용자 ID의 존재 여부 확인
     *
     * 클라이언트가 전송한 암호화된 사용자 ID가 서버에 존재하는지 확인합니다.
     * 주로 인증/인가 과정에서 활용됩니다.
     *
     * @param userIdDto 확인할 사용자 ID 정보
     * @return 사용자 ID 존재 여부
     */
    @PostMapping("/check-userId")
    public ResponseEntity<String> checkUserId(@RequestBody UserIdDto userIdDto) {
        String userId = userIdDto.getUserId();

        if (userService.checkEncrptedUserIdExists(userId)) {
            return ResponseEntity.ok("User ID exists.");
        }
        return ResponseEntity.badRequest().body("User ID does not exist.");
    }
}