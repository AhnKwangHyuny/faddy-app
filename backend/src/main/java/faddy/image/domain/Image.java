package faddy.image.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import faddy.global.BaseEntity;
import faddy.image.type.ImageCategory;
import faddy.snap.domain.Snap;
import faddy.styleBoard.domain.StyleBoard;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id", nullable = false)
    private Long id;

    @Column(name = "image_url", nullable = false, length=1024)
    private String imageUrl;

    @Column(name = "hash_name", nullable = false  , unique = true)
    private String hashName;

    @Column(name = "image_name", nullable = false)
    private String originalName;

    @Column(name = "image_size")
    private Long size; // Assuming the size is in bytes

    @Column(name = "image_format", nullable = false)
    private String format; // e.g., "jpeg", "png", etc.

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "snap_id")
    private Snap snap;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private Snap chat;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY , cascade = CascadeType.REMOVE)
    @JoinColumn(name = "style_board_id")
    private StyleBoard styleBoard;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private ImageCategory category = ImageCategory.ETC;


    public Image(String imageUrl, String hashName, String originalName, Long size, String format , ImageCategory category) {
        this.imageUrl = imageUrl;
        this.hashName = hashName;
        this.originalName = originalName;
        this.size = size;
        this.format = format;
        this.category = category;
    }

    public void linkStyleBoard(StyleBoard styleBoard) {
        this.styleBoard = styleBoard;
    }

    public void addSnap(Snap snap) {
        this.snap = snap;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", imageUrl='" + imageUrl + '\'' +
                ", hashedName='" + hashName + '\'' +
                '}';
    }
}
