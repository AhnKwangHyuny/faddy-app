package faddy.comment.respository;

import faddy.comment.domain.Comment;
import faddy.snap.domain.Snap;
import faddy.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 스냅에 대한 모든 댓글 조회 (
    @Query("SELECT c FROM Comment c " +
            "JOIN FETCH c.snap s " +
            "JOIN FETCH c.user u " +
            "WHERE c.snap = :snap " +
            "ORDER BY c.created_at, c.updated_at DESC")
    List<Comment> findAllBySnapWithUser(Snap snap);

    // 특정 댓글의 자식 댓글 조회
    @Query("SELECT c FROM Comment c " +
            "JOIN FETCH c.snap s " +
            "JOIN FETCH c.user u " +
            "WHERE c.parent = :parent")
    List<Comment> findAllByParentWithUser(Comment parent);

    // 특정 사용자가 작성한 모든 댓글 조회
    List<Comment> findAllByUser(User user);

    // 댓글 삭제
    @Transactional
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.id = :commentId")
    void deleteComment(Long commentId);
//
//    // 댓글 수정
//    @Transactional
//    @Modifying
//    @Query("UPDATE Comment c SET c.content = :content WHERE c.id = :commentId")
//    void updateComment(Long commentId, String content);

}
