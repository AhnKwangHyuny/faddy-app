package faddy.category.domain;

import faddy.global.BaseEntity;
import faddy.snap.domain.Snap;
import faddy.type.ContentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "categories")
@NoArgsConstructor(access = PROTECTED)
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> children = new ArrayList<>();

    @OneToMany(mappedBy = "category" , cascade = CascadeType.REMOVE , fetch = FetchType.LAZY)
    private List<CategorySnap> categorySnaps = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ContentType contentType;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category category)) return false;
        return Objects.equals(id, category.id)
                && Objects.equals(name, category.name)
                && Objects.equals(parent, category.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, parent);
    }

    public Category(String name, Category parent) {
        this.name = name;
        this.parent = parent;
        if (parent != null) {
            parent.addChildCategory(this);
        }
    }

    public Category(String name, Category parent, ContentType contentType) {
        this.name = name;
        this.parent = parent;
        this.contentType = contentType;
    }

    public void addChildCategory(Category child) {
        children.add(child);
        child.parent = this;
    }

    public void addSnap(Snap snap) {
        CategorySnap categorySnap = new CategorySnap(this , snap);
        categorySnaps.add(categorySnap);
        snap.getCategorySnaps().add(categorySnap);
    }

}
