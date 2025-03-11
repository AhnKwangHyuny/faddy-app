package faddy.like.domain;

import faddy.global.BaseEntity;
import faddy.like.type.ContentType;
import faddy.snap.domain.Snap;
import faddy.styleBoard.domain.StyleBoard;
import faddy.styleBoardComment.domain.StyleBoardComment;
import faddy.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "likes")
public class Like extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY , cascade = CascadeType.REMOVE)
    @JoinColumn(name = "snap_id")
    private Snap snap;

    @ManyToOne(fetch = FetchType.LAZY  , cascade = CascadeType.REMOVE)
    @JoinColumn(name = "style_board_id")
    private StyleBoard styleBoard;

    @ManyToOne(fetch = FetchType.LAZY , cascade = CascadeType.REMOVE)
    @JoinColumn(name = "style_board_comment_id")
    private StyleBoardComment styleBoardComment;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type")
    private ContentType contentType;

    public Like(User user, Snap snap) {
        this.user = user;
        this.snap = snap;
    }

    // 생성 메소드
    public static Like createLikeForSnap(User user, Snap snap) {
        Like like = new Like();
        like.user = user;
        like.snap = snap;
        like.contentType = ContentType.SNAP;
        return like;
    }

    public static Like createLikeForStyleBoard(User user, StyleBoard styleBoard) {
        Like like = new Like();
        like.user = user;
        like.styleBoard = styleBoard;
        like.contentType = ContentType.STYLE_BOARD;
        return like;
    }

    public static Like createLikeForStyleBoardComment(User user, StyleBoardComment styleBoardComment) {
        Like like = new Like();
        like.user = user;
        like.styleBoardComment = styleBoardComment;
        like.contentType = ContentType.STYLE_BOARD_COMMENT;
        return like;
    }




    // 연관관계 메소드
    public void associateSnap(Snap snap) {
        this.snap = snap;
    }

    public void associateStyleBoard(StyleBoard styleBoard) {
        this.styleBoard = styleBoard;
    }

    public void associateUser(User user) {
        this.user = user;
    }

    public void associateStyleBoardComment(StyleBoardComment comment) {
        this.styleBoardComment = comment;
    }
}