package faddy.filter;

import faddy.auth.dto.CustomUserDetails;
import faddy.auth.jwt.Service.JwtUtil;
import faddy.auth.jwt.Service.TokenBlackListService;
import faddy.user.domain.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final TokenBlackListService tokenBlackListService;

    public JwtFilter(JwtUtil jwtUtil, TokenBlackListService tokenBlackListService) {
        this.jwtUtil = jwtUtil;
        this.tokenBlackListService = tokenBlackListService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Request에서 Authorization 헤더를 찾음

        String authorization = request.getHeader("Authorization");

        // Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            // 최초 로그인
            filterChain.doFilter(request, response);

            return;
        }

        // Bear 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];

        if (token == null) {
            log.warn(" token not exists");

            // 토큰의 유효성 검증 결과 저장
            request.setAttribute("isTokenValid" , false);

            filterChain.doFilter(request, response);
        }

        //토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {

            request.setAttribute("isTokenValid" , false);

            filterChain.doFilter(request, response);

            return;
        }

        //토큰에서 username과 role 획득
        String username = jwtUtil.getUsername(token);

        //userEntity를 생성하여 값 set
        User userEntity = new User.Builder()
                .withNickname("mock")
                .withEmail("mock.naver.com")
                .withPassword("")
                .withUsername(username)
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(userEntity);

        // 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);


        request.setAttribute("isTokenValid" , true);
        filterChain.doFilter(request, response);
    }
}
