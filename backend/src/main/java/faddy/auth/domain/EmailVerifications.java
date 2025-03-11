//package faddy.auth.domain;
//
//import faddy.global.BaseEntity;
//import faddy.user.domain.User;
//import jakarta.persistence.*;
//import lombok.AccessLevel;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//
//@Entity
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Table(name = "EmailVerifications")
//public class EmailVerifications extends BaseEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "EmailVerifications_id")
//    private Long id;
//
//    @OneToOne(cascade = CascadeType.REMOVE)
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    @Column(name = "verification_code", nullable = false)
//    private String verificationCode;
//
//    @Column(name = "verified", nullable = false)
//    private Boolean verified;
//
//    @Column(name = "verified_at")
//    private LocalDateTime verifiedAt;
//
//    @Column(name = "email", unique = true)
//    private String email;
//
//    @Enumerated
//    @Column(name = "verification_email_platform", length = 10 ,nullable = false)
//    private Verification_email_platform verification_email_platform;
//}
