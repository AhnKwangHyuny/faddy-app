package faddy.styleBoardComment.domain;

import faddy.global.BaseEntity;
import faddy.styleBoard.domain.StyleBoard;
import faddy.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(callSuper = true)
@Table(name = "style_board_comments")
@SQLDelete(sql = "UPDATE style SET status = 'DELETED' WHERE id = ?") // 논리 삭제 채택
public class StyleBoardComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "style_board_id", nullable = false)
    private StyleBoard styleBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @Lob
    @Column(name = "content", nullable = false , columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private StyleBoardComment parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StyleBoardComment> children = new ArrayList<>();

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Column(name = "likes", nullable = false)
    private int likes = 0;

    // 댓글 생성자
    public StyleBoardComment(StyleBoard styleBoard, User user, String content) {
        this.styleBoard = styleBoard;
        this.author = user;
        this.content = content;
    }

    @Builder
    public StyleBoardComment(StyleBoard styleBoard, User author, String content, StyleBoardComment parent) {
        this.styleBoard = styleBoard;
        this.author = author;
        this.content = content;
        if (parent != null) {
            this.parent = parent;
        }
    }



    // 대댓글 생성 메서드
    public static StyleBoardComment createReply(StyleBoardComment parent, User author, String content , StyleBoard styleBoard) {

        StyleBoardComment reply = new StyleBoardComment(parent.getStyleBoard(), author, content);
        reply.parent = parent;
        parent.addChild(reply);
        return reply;
    }

    // 대댓글을 위한 연관관계 설정
    private void addChild(StyleBoardComment child) {
        if (this.parent != null) {
            throw new IllegalArgumentException("대댓글에는 대댓글을 달 수 없습니다.");
        }
        this.children.add(child);
    }


    // 댓글 삭제 메서드
    public void delete() {
        this.isDeleted = true;
        this.content = "삭제된 댓글입니다.";
    }


}
