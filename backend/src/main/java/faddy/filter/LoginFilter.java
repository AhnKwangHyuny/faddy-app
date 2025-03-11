package faddy.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import faddy.auth.api.response.AuthTokensResponse;
import faddy.auth.dto.CustomUserDetails;
import faddy.auth.dto.LoginRequestDto;
import faddy.auth.jwt.Service.JwtUtil;
import faddy.authToken.infrastructure.AuthTokensGenerator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@PropertySource("classpath:application.yml")
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AuthTokensGenerator authTokensGenerator;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, AuthTokensGenerator authTokensGenerator) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.authTokensGenerator = authTokensGenerator;
    }

    @Value("${spring.jwt.token.access.expire-time}")
    private Long ACCESS_EXPIRE_TIME;

    @Value("${spring.jwt.token.refresh.expire-time}")
    private Long REFRESH_EXPIRE_TIME;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginRequestDto loginRequestDto;

        try {
            loginRequestDto = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);
        } catch (IOException e) {
            throw new AuthenticationServiceException("Failed to parse login request", e);
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());

        return authenticationManager.authenticate(authToken);
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();

        if (username.isEmpty()) {
            sendErrorResponse(request, response, "Username cannot be empty", HttpStatus.BAD_REQUEST);
            return;
        }
        AuthTokensResponse tokenResponse = authTokensGenerator.generate(username);

        // 클라이언트에 토큰 발급
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.getWriter().write(objectMapper.writeValueAsString(tokenResponse));
        response.getWriter().flush();

    }

    private void sendErrorResponse(HttpServletRequest request, HttpServletResponse response, String errorMessage, HttpStatus status) throws IOException {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("status", status.value());
        errorDetails.put("error", status.getReasonPhrase());
        errorDetails.put("message", errorMessage);
        errorDetails.put("path", request.getRequestURI());

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
