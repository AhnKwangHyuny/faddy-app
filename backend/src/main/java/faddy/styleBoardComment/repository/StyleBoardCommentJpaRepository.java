package faddy.styleBoardComment.repository;

import faddy.styleBoardComment.domain.StyleBoardComment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface StyleBoardCommentJpaRepository extends JpaRepository<StyleBoardComment, Long> {
    @Query("SELECT DISTINCT c FROM StyleBoardComment c " +
            "LEFT JOIN FETCH c.author a " +
            "LEFT JOIN FETCH a.profile " +
            "LEFT JOIN FETCH c.children ch " +
            "LEFT JOIN FETCH ch.author cha " +
            "LEFT JOIN FETCH cha.profile " +
            "WHERE c.styleBoard.id = :styleBoardId AND c.parent IS NULL")
    List<StyleBoardComment> findByStyleBoardIdWithChildren(@Param("styleBoardId") Long styleBoardId);

    Optional<StyleBoardComment> findById(Long id);

    @Query("SELECT c FROM StyleBoardComment c ")
    List<StyleBoardComment> findAllStyleBoardComment();

    @Modifying
    @Transactional
    @Query("DELETE FROM StyleBoardComment c WHERE c.styleBoard.id = :styleBoardId")
    void deleteAllByStyleBoardId(@Param("styleBoardId") Long styleBoardId);
}
