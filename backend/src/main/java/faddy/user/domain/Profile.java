package faddy.user.domain;

import faddy.global.BaseEntity;
import faddy.profile.domain.UserLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static faddy.global.Constants.DEFAULT_PROFILE_IMAGE_URL;
import static faddy.global.Constants.DEFAULT_PROFILE_MOTTO;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "Profiles")
@NoArgsConstructor(access = PROTECTED)
public class Profile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id", nullable = false)
    private Long id;

    @Column(name = "motto")
    private String motto = DEFAULT_PROFILE_MOTTO;

    @Column(name = "grade")
    @Enumerated(EnumType.STRING)
    private UserLevel userLevel = UserLevel.LEVEL_1;

    @Column(name = "profile_image_url")
    private String profileImageUrl = DEFAULT_PROFILE_IMAGE_URL;

    public Profile(String motto) {
        this.motto = motto;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", motto='" + motto + '\'' +
                ", userLevel=" + userLevel +
                '}';
    }

    public void levelUp() {
        UserLevel nextLevel = this.userLevel.getNextLevel();

        if (nextLevel != null) {
            this.userLevel = nextLevel;
        }
    }

}