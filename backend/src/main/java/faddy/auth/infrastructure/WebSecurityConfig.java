package faddy.auth.infrastructure;

import faddy.auth.handler.CustomAccessDeniedHandler;
import faddy.auth.handler.EntryPointUnauthorizedHandler;
import faddy.auth.jwt.Service.JwtUtil;
import faddy.auth.jwt.Service.TokenBlackListService;
import faddy.authToken.infrastructure.AuthTokensGenerator;
import faddy.filter.JwtFilter;
import faddy.filter.LoginFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;

    private final JwtUtil jwtUtil;

    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final EntryPointUnauthorizedHandler entryPointUnauthorizedHandler;

    private final TokenBlackListService tokenBlackListService;

    private final AuthTokensGenerator authTokensGenerator;

    //AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
    public WebSecurityConfig(AuthenticationConfiguration authenticationConfiguration, JwtUtil jwtUtil,
                             CustomAccessDeniedHandler customAccessDeniedHandler,
                             EntryPointUnauthorizedHandler entryPointUnauthorizedHandler, TokenBlackListService tokenBlackListService,
                             AuthTokensGenerator authTokensGenerator) {

        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.entryPointUnauthorizedHandler = entryPointUnauthorizedHandler;
        this.tokenBlackListService = tokenBlackListService;
        this.authTokensGenerator = authTokensGenerator;
    }

    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        LoginFilter loginFilter = new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, authTokensGenerator);
        loginFilter.setFilterProcessesUrl("/api/v1/users/login"); // 로그인 요청을 처리할 URL 설정


        http.exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    // 인증 실패 시 처리 로직
                    new EntryPointUnauthorizedHandler().commence(request, response, authException);
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    // 권한 거부 시 처리 로직
                    new CustomAccessDeniedHandler().handle(request, response, accessDeniedException);
                })
        );

        //csrf disable
        http
                .csrf((auth) -> auth.disable())
                .cors(corsCustomize -> corsCustomize.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowCredentials(true);
                    config.setAllowedOriginPatterns(Collections.singletonList("*"));
                    config.addAllowedMethod(HttpMethod.OPTIONS);
                    config.addAllowedMethod(HttpMethod.GET);
                    config.addAllowedMethod(HttpMethod.POST);
                    config.addAllowedMethod(HttpMethod.PUT);
                    config.addAllowedMethod(HttpMethod.DELETE);
                    config.addAllowedHeader("*");
                    config.addExposedHeader("*");  // Add this line
                    return config;
                }));

        //From 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        //http basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());


        http
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/snaps").authenticated()
                                .anyRequest().permitAll()
                )
                .addFilterBefore(new JwtFilter(jwtUtil ,tokenBlackListService ) , LoginFilter.class)
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);

        //세션 설정
        http
                .sessionManagement((session) -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();

    }

    // webSocket configuration
    // client react를 사용하므로 모든 url cors 허용
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOriginPatterns("*");
            }
        };
    }

}
