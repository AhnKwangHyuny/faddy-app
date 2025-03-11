package faddy.auth.presentation;

import faddy.auth.jwt.Service.JwtUtil;
import faddy.email.service.MailService;
import faddy.global.Utils.RedisUtil;
import faddy.api.Dto.ResponseDto;
import faddy.api.response.AuthCodeVerificationResult;
import faddy.global.exception.ExceptionCode;
import faddy.global.exception.ServerProcessingException;
import faddy.user.dto.request.AuthCodeAndEmailDto;
import faddy.user.dto.request.EmailRequestDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth-codes")
public class AuthCodeController {

    private final MailService mailService;
    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;

    private final String BEARER = "Bearer ";
    private final String AUTHENTICATION = "Authentication" ;

    private final long SIGNUP_TOKEN_EXPIRE_TIME = 30 * 60 * 1000; // 30분

    /**
     * 인증 코드 검증 및 인증 토큰 발급.
     *
     * @Description
     * 사용자가 제공한 이메일 인증 코드 검증 후 유효한 경우 인증토큰 생성.
     * 생성된 토큰은 헤더와 응답 본문에 모두 포함하여 반환.
     * 토큰 유효 기간은 30분으로 설정.
     *
     * @Request
     * - email: 인증 이메일 주소
     * - code: 사용자가 입력한 인증 코드
     *
     * @Response
     * - 200: 인증토큰 발급 성공 (헤더 Authentication 필드에 토큰 포함)
     * - 500: 토큰 생성 실패
     *
     * @param request 이메일과 인증 코드를 포함하는 DTO
     * @param response HTTP 응답 객체
     * @return 인증 토큰을 포함한 ResponseEntity 객체
     * @throws NoSuchAlgorithmException 암호화 알고리즘 관련 예외 발생 시
     */
    @PostMapping("/verify")
    public ResponseEntity verifyEmailAuthCode(@RequestBody @Valid AuthCodeAndEmailDto request , HttpServletResponse response) throws NoSuchAlgorithmException {

        String email = request.getEmail();
        String code = request.getCode();
        String token = null;

        AuthCodeVerificationResult result = mailService.verifiedCode(email, code);
        try {
            if(result.getResult()) {

                // jwt 인증 토큰 생성
                final long now = System.currentTimeMillis();
                Date expiredAt = new Date(now + SIGNUP_TOKEN_EXPIRE_TIME);

                token = jwtUtil.generate(email, expiredAt);

            }

        } catch (Exception e) {
            throw new ServerProcessingException(ExceptionCode.TOKEN_GENERATION_ERROR);
        }

        if(token == null) {
            throw new ServerProcessingException(ExceptionCode.TOKEN_GENERATION_ERROR);
        }

        // responseDto 객체 생성
        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTHENTICATION , BEARER + token);

        Map<String, String> data = new HashMap<>();
        data.put("authentication", BEARER + token);


        return ResponseEntity.status(200)
                .headers(headers)
                .body(ResponseDto.response(
                                "200",
                                "인증토큰 발급이 완료되었습니다.",
                                data
                        )
                );
    }

    /**
     * 이메일 인증 코드 발송.
     *
     * @Description
     * 클라이언트가 요청한 이메일 주소 검증 후 인증 코드 생성 및 발송.
     * 이메일 형식 검증 및 중복 검사 수행.
     * 인증 코드는 Redis에 저장되어 만료 시간 관리.
     *
     * @Request
     * - email: 인증 코드를 발송할 이메일 주소
     *
     * @Response
     * - 200: 인증 코드 발송 성공
     * - 400: 유효하지 않은 이메일 주소
     *
     * @param emailDto 이메일 주소를 포함하는 DTO 객체
     * @return 인증 코드 발송 결과 상태 코드
     * @throws NoSuchAlgorithmException 암호화 알고리즘 관련 예외 발생 시
     */
    @PostMapping
    public ResponseEntity sendMessage(@RequestBody EmailRequestDto emailDto) throws NoSuchAlgorithmException {

        String email = emailDto.getEmail();

        if(email.isEmpty() || !mailService.isValidEmail(email)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        mailService.sendCodeToMail(email); // 이메일 유효성 , 중복 검사 후 인증코드 발송
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 인증 코드 삭제.
     *
     * @Description
     * 인증 시간 만료 또는 사용자 요청 시 Redis에 저장된 인증 코드 삭제.
     * 유효한 이메일 주소와 Redis에 해당 키가 존재하는지 검증.
     *
     * @Request
     * - email: 인증 코드가 저장된 이메일 주소
     *
     * @Response
     * - 200: 인증 코드 삭제 성공
     * - 400: 유효하지 않은 이메일 주소 또는 인증 코드 없음
     *
     * @param emailDto 이메일 주소를 포함하는 DTO 객체
     * @return 인증 코드 삭제 결과 메시지
     */
    @DeleteMapping
    public ResponseEntity deleteAuthCode(@RequestBody EmailRequestDto emailDto) {

        String email = emailDto.getEmail();

        String key = mailService.createKey(email);

        if(email.isEmpty() || !mailService.isValidEmail(email) || !redisUtil.hasKey(key) )  {

            return ResponseEntity.badRequest().body(
                    ResponseDto.response(
                            "400",
                            "유효한 이메일이 아닙니다. 확인 후 재 요청 부탁드립니다."
                    )
            );
        }

        redisUtil.deleteData(key);

        return ResponseEntity.status(200)
                .body(ResponseDto.response(
                        "200",
                        "정상적으로 삭제되었습니다."
                ));
    }
}