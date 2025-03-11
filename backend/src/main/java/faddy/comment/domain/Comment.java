package faddy.comment.domain;

import faddy.global.BaseEntity;
import faddy.snap.domain.Snap;
import faddy.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name ="comments")
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "snap_id")
    private Snap snap;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "content" , nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent; // 부모 댓글

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> children = new ArrayList<>(); //자식 댓글

    static public Comment createComment(Snap snap, User user, String content) {
        Comment comment = new Comment();
        comment.snap = snap;
        comment.user = user;
        comment.content = content;
        comment.parent = null; // 초기값

        return comment;
    }

    //양방향 연관관계
    public void setParent(Comment comment) {
        this.parent = comment;
        comment.getChildren().add(this);
    }

    //댓글 수정
    public void edit(String content) {
        this.content = content;
    }

}
