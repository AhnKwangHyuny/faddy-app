package faddy.like.repository;

import faddy.like.domain.Like;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class LikeCustomRepositoryImpl implements LikeCustomRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Long> batchSaveLikesForSnaps(List<Like> likes) {
        log.info("Batch saving likes for snaps: {}", likes);
        List<Long> result = executeBatchSave(likes, "INSERT INTO likes (user_id, snap_id, content_type) VALUES (?, ?, ?)");
        log.info("Batch save for snaps completed. Result: {}", result);
        return result;
    }

    @Override
    public List<Long> batchSaveLikesForStyleBoards(List<Like> likes) {
        log.info("Batch saving likes for style boards: {}", likes);
        List<Long> result = executeBatchSave(likes, "INSERT INTO likes (user_id, style_board_id, content_type , created_at) VALUES (?, ?, ?, ?)");
        log.info("Batch save for style boards completed. Result: {}", result);
        return result;
    }

    @Override
    public List<Long> batchSaveLikesForStyleBoardComments(List<Like> likes) {
        log.info("Batch saving likes for style board comments: {}", likes);
        List<Long> result = executeBatchSave(likes, "INSERT INTO likes (user_id, style_board_comment_id, content_type) VALUES (?, ?, ?)");
        log.info("Batch save for style board comments completed. Result: {}", result);
        return result;
    }

    private List<Long> executeBatchSave(List<Like> likes, String sql) {
        log.info("Executing batch save with SQL: {}", sql);

        return Arrays.stream(
                jdbcTemplate.batchUpdate(
                    sql,
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            Like like = likes.get(i);
                            log.debug("Setting values for like: {}", like);
                            ps.setLong(1, like.getUser().getId());
                            switch (like.getContentType()) {
                                case SNAP -> ps.setLong(2, like.getSnap().getId());
                                case STYLE_BOARD -> ps.setLong(2, like.getStyleBoard().getId());
                                case STYLE_BOARD_COMMENT -> ps.setLong(2, like.getStyleBoardComment().getId());
                            }
                            ps.setString(3, like.getContentType().name());
                            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now())); // 현재 시간으로 created_at 설정

                        }

                        @Override
                        public int getBatchSize() {
                            return likes.size();
                        }
                    }
                ))
        .boxed()
        .map(Integer::longValue)
        .toList();
    }
}
