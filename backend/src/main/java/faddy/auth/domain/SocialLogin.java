package faddy.auth.domain;

import faddy.global.BaseEntity;
import faddy.user.domain.User;
import jakarta.persistence.*;

@Entity
@Table(name = "Social_Login")
public class SocialLogin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "social_login_id", nullable = false)
    private int socialLoginId;

//    @OneToOne(cascade = CascadeType.REMOVE)
//    @JoinColumn(name = "user_id")
//    private User user;


    @Column(name = "social_code")
    private byte socialCode; // Assuming tinyint maps to byte

    @Column(name = "external_id", length = 64)
    private String externalId;

    @Column(name = "access_token", length = 255)
    private String accessToken;



    // getters and setters
}
