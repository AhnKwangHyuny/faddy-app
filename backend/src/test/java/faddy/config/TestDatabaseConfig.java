package faddy.config;


import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;


/**
 * 테스트용 데이터베이스 설정 클래스
 */
@TestConfiguration
@Profile("test")
@ActiveProfiles("test")
public class TestDatabaseConfig {
    /**
     * 테스트용 데이터소스를 생성합니다.
     * 실제 데이터베이스 대신 테스트용 데이터베이스를 사용합니다.
     *
     * @return 테스트용 데이터소스
     */
    @Bean
    @Primary
    public DataSource dataSource(){
        return DataSourceBuilder.create()
                .url("jdbc:mysql://localhost:3306/faddy_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true")
                .username("root")
                .password("Agh@p970314")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }
}
