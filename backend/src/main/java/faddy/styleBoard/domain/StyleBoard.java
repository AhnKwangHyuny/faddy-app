package faddy.styleBoard.domain;

import faddy.global.BaseEntity;
import faddy.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(callSuper = true)
@Getter
@Table(name = "style_boards")
public class StyleBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "style_board_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(length = 60, nullable = false)
    private String title;

    @Lob
    @Column(name = "content", columnDefinition = "TEXT" , nullable = false)
    private String content;

    @Column(name = "views")
    @Min(0)
    private int views = 0;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    public StyleBoard(Category category, String title, String content, User author) {
        this.category = category;
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public StyleBoard(Category category, String title, String content) {
        this.category = category;
        this.title = title;
        this.content = content;
    }

    /**
     *  entity methods
     * */

    public static StyleBoardBuilder builder() {
        return new StyleBoardBuilder();
    }

    public static class StyleBoardBuilder {
        private Category category;
        private String title;
        private String content;

        private StyleBoardBuilder() {
        }

        public StyleBoardBuilder withCategory(Category category) {
            this.category = category;
            return this;
        }

        public StyleBoardBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public StyleBoardBuilder withContent(String content) {
            this.content = content;
            return this;
        }

        public StyleBoard build() {
            return new StyleBoard(category, title, content);
        }
    }

    // 업데이트 메서드 추가
    public void update(String title, String content ) {
        this.title = title;
        this.content = content;
    }


    public void linkToAuthor(User author) {
        this.author = author;
    }

    public void updateViewCount (int views) {
        this.views = views;
    }

}
