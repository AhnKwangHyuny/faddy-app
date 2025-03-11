package faddy.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * JPA 테스트를 위한 설정 클래스
 * JPA Auditing 기능을 활성화하고 테스트에 필요한 빈들을 구성합니다.
 */
@TestConfiguration
@EnableJpaAuditing
public class JpaTestConfig {

    /**
     * JPA Auditing의 생성자, 수정자 정보를 제공하는 빈
     * 테스트 환경에서는 'test-system'을 기본값으로 사용
     *
     * @return 테스트용 AuditorAware 구현체
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of("test-system");
    }
}