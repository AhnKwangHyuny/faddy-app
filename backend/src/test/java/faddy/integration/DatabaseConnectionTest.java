package faddy.integration;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


/**
 * MySQL 데이터베이스 연결 테스트
 */


@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
public class DatabaseConnectionTest {

    @Autowired
    private DataSource dataSource;

    /**
     * 데이터베이스 연결이 성공적으로 이루어지는지 테스트합니다.
     */

    @Test
    public void testDatabaseConnection() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            assertThat(connection).isNotNull();
            assertThat(connection.isValid(1)).isTrue();

            System.out.println("데이터 베이스 연결 성공:" + connection.getMetaData().getURL());
        }
    }
}
