package faddy.comment.service;

import faddy.comment.domain.Comment;
import faddy.comment.respository.CommentRepository;
import faddy.snap.domain.Snap;
import faddy.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentServiceImpl {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    /**
     * 특정 스냅에 대한 댓글 생성
     *
     * @param snap 댓글이 속한 스냅 엔티티
     * @param user 댓글 작성자 사용자 엔티티
     * @param content 댓글 내용
     * @return 생성된 댓글 엔티티
     */
    public Comment createComment(Snap snap, User user, String content) {
        Comment comment = Comment.createComment(snap, user, content);
        return commentRepository.save(comment);
    }

    /**
     * 특정 스냅에 대한 모든 댓글 조회 (자식 댓글 포함)
     *
     * @param snap 댓글 조회 대상 스냅 엔티티
     * @return 해당 스냅에 대한 모든 댓글 목록 (자식 댓글 포함)
     */
    public List<Comment> findAllBySnapWithUser(Snap snap) {
        return commentRepository.findAllBySnapWithUser(snap);
    }

    /**
     * 특정 댓글의 자식 댓글 조회 (자식 댓글 포함)
     *
     * @param parent 부모 댓글 엔티티
     * @return 해당 부모 댓글의 자식 댓글 목록 (자식 댓글 포함)
     */
    public List<Comment> findAllByParentWithUser(Comment parent) {
        return commentRepository.findAllByParentWithUser(parent);
    }

    /**
     * 특정 사용자가 작성한 모든 댓글 조회
     *
     * @param user 댓글 작성자 사용자 엔티티
     * @return 해당 사용자가 작성한 모든 댓글 목록
     */
    public List<Comment> findAllByUser(User user) {
        return commentRepository.findAllByUser(user);
    }

    /**
     * 댓글 삭제
     *
     * @param commentId 삭제할 댓글 ID
     */
    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteComment(commentId);
    }

    /**
     * 댓글 수정
     *
     * @param commentId 수정할 댓글 ID
     * @param content 수정된 댓글 내용
     */
    @Transactional
    @Modifying
    public void updateComment(Long commentId, String content) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        comment.edit(content);
        commentRepository.save(comment);
    }
}
