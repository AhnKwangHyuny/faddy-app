package faddy.authToken.presentation;

import faddy.auth.api.response.AuthTokensResponse;
import faddy.auth.jwt.Service.JwtUtil;
import faddy.authToken.service.ManagerReissueTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auths")
public class ManagerReissueTokenController {

    private final ManagerReissueTokenService managerReissueTokenService;

    private final JwtUtil jwtUtil;

    private final String ERROR_MESSAGE = "톤큰이 존재하지 않거나 유효한 토큰 형식이 아닙니다.";

    @PostMapping("/reissue-token")
    public ResponseEntity reissueToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        // 1. 요청 헤더에서 Authorization 헤더 값 가져오기
        String refreshToken = jwtUtil.extractRawToken(request.getHeader("Authorization"));

        if(refreshToken == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ERROR_MESSAGE);
        }

        // 2. 서비스 단에서 토큰 재발급 로직 수행 (새 access , refresh 토큰 반환)
        AuthTokensResponse tokenResponse = managerReissueTokenService.reissueToken(refreshToken);
        System.out.println("tokenResponse.toString() = " + tokenResponse.toString());
        return ResponseEntity.ok(tokenResponse);
    }
}
