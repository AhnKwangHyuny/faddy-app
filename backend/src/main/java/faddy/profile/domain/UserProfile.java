package faddy.profile.domain;

import faddy.image.domain.Image;
import faddy.user.domain.User;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "user_profile")
@NoArgsConstructor
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "motto")
    private String motto;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_image_id", nullable = false)
    private Image profileImage;

    @Column(name = "user_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserLevel userLevel = UserLevel.LEVEL_1; // 기본값 LEVEL_1

}