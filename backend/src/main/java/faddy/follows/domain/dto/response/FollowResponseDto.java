package faddy.follows.domain.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FollowResponseDto {
    private String userId;

    private String nickname;

    private String profileImageUrl;

    public static class Builder {
        private String userId;
        private String nickname;
        private String profileImageUrl;

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public Builder profileImageUrl(String profileImageUrl) {
            this.profileImageUrl = profileImageUrl;
            return this;
        }

        public FollowResponseDto build() {
            return new FollowResponseDto(userId, nickname, profileImageUrl);
        }
    }

    private FollowResponseDto(String userId, String nickname, String profileImageUrl) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    @Override
    public String toString() {
        return "FollowResponseDto{" +
                "userId='" + userId + '\'' +
                ", nickname='" + nickname + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                '}';
    }
}