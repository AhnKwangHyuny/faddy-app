package faddy.hashTags.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import faddy.global.BaseEntity;
import faddy.hashTags.types.ContentType;
import faddy.snap.domain.Snap;
import faddy.styleBoard.domain.StyleBoard;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "hashTags")
@NoArgsConstructor(access = PROTECTED)
public class HashTag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashTag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnore
    @JoinColumn(name = "snap_id")
    private Snap snap;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "style_board_id")
    private StyleBoard styleBoard;

    @Column(name = "name", nullable = false, length = 10)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ContentType type;

    @Column(name = "order_num", nullable = false)
    @Min(0)
    @Max(4)
    private int priority;

    public HashTag(String name, int priority, ContentType type) {
        this.name = name;
        this.type = type;
        this.priority = priority;
    }

    public void linkToStyleBoard(StyleBoard styleBoard) {
        this.styleBoard = styleBoard;
    }


    public void updatePriority(int priority) {
        this.priority = priority;
    }

    //addSnap
    public void linkToSnap(Snap snap) {
        this.snap = snap;
        snap.getHashTags().add(this);
    }


    @Override
    public String toString() {
        return "HashTag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", priority=" + priority +
                ", type=" + type +
                '}';
    }
}
