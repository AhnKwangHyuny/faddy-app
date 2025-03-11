package faddy.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import faddy.follows.domain.Follow;
import faddy.global.BaseEntity;
import faddy.global.exception.AuthorizationException;
import faddy.global.exception.ExceptionCode;
import faddy.like.domain.Like;
import faddy.snap.domain.Snap;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = PROTECTED)
@SQLDelete(sql = "UPDATE member SET status = 'DELETED' WHERE id = ?")
public class User extends BaseEntity  {

    public User(String username, String password, String nickname, String email) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @OneToOne(fetch= FetchType.LAZY , cascade = CascadeType.REMOVE)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL , fetch = FetchType.LAZY , orphanRemoval = true)
    private List<Snap> snaps = new ArrayList<>();


    @Column(name = "username", length = 128, unique = true, nullable = false)
    private String username;

    @Column(name = "password", length = 255, nullable = false)
    private String password;


    @Column(name = "nickname", length = 20, unique = true , nullable = false)
    private String nickname;

    @Column(name = "email" , length =  255 , unique = true , nullable = false)
    private String email;

    @Enumerated(value = STRING)
    private UserStatus status;

    @Enumerated(value = STRING)
    private Authority authority;

    // User.java
    @OneToMany(mappedBy = "following" , cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    private List<Follow> followings = new ArrayList<>();

    @OneToMany( mappedBy = "follower" ,  cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true , fetch = FetchType.LAZY)
    private List<Like> likes = new ArrayList<>();

    /**
     * @Param entity method
     * */

    public User(Profile profile, String username, String password, String nickname , String email) {
        this.profile = profile;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
    }

    public User(String username, String password, String nickname, String email, Authority authority) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.authority = authority;
    }

    // test builder
    public static class Builder {
        private String username;
        private String nickname;

        private String password;

        private String email;

        private Authority authority;

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder withNickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withAuthority(String authority) {
            try {
                this.authority = Authority.valueOf(authority.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new AuthorizationException(ExceptionCode.AUTHENTICATION_ERROR);
            }
            return this;
        }


        public User build() {
            return new User(username, password , nickname, email , authority );
        }
    }

    // 연관관계 메소드
    public void addSnap(Snap snap) {
        this.snaps.add(snap);
    }

    public void addLike(Like like) {
        this.likes.add(like);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}