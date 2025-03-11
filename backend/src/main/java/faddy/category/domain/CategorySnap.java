package faddy.category.domain;

import faddy.global.BaseEntity;
import faddy.snap.domain.Snap;
import jakarta.persistence.*;

@Entity
@Table(name = "category_snap")
public class CategorySnap extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_snap_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "snap_id")
    private Snap snap;

    public CategorySnap(Category category, Snap snap) {
        this.category = category;
        this.snap = snap;
    }

    public CategorySnap() {

    }
}
