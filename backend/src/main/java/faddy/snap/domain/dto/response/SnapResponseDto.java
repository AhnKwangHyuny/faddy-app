package faddy.snap.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(using = JsonDeserializer.class)
public class SnapResponseDto {
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UserDto user;

    @NotNull
    @Size(min = 1)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ImageDto> images;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<HashTagDto> hashTags;

    @Size(max = 1500)
    private String description;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created_at;

    @Override
    public String toString() {
        return "SnapResponseDto{" +
                "user=" + (user!= null? user.toString() : "null") +
                ", images=" + (images!= null? images.stream().map(ImageDto::toString).collect(Collectors.joining(", ")) : "null") +
                ", hashTags=" + (hashTags!= null? hashTags.stream().map(HashTagDto::toString).collect(Collectors.joining(", ")) : "null") +
                ", description='" + description + '\'' +
                ", createdAt=" + created_at +
                '}';
    }


    @Getter
    @NoArgsConstructor
    public static class UserDto {
        @NotNull
        private Long id;

        @NotBlank
        @Size(max = 128)
        @JsonProperty("username")
        private String username;

        @NotBlank
        @Size(max = 20)
        private String nickname;

        @NotNull
        private ProfileDto profile;

        @NotBlank
        @Size(max = 255)
        private String email;

        @Builder
        public UserDto(Long id, String username, String nickname, String email, ProfileDto profile) {
            this.id = id;
            this.username = username;
            this.nickname = nickname;
            this.email = email;
            this.profile = profile; // 생성자에 profile 추가
        }

        // toString() 메서드도 수정
        @Override
        public String toString() {
            return "UserDto{" +
                    "id=" + id +
                    ", username='" + username + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", email='" + email + '\'' +
                    ", profile=" + profile + // profile 추가
                    '}';
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ProfileDto {
        @NotNull
        private Long id;

        @Size(max = 50)
        private String motto;

        @NotBlank
        private String userLevel;

        @Builder
        public ProfileDto(Long id, String motto, String userLevel) {
            this.id = id;
            this.motto = motto;
            this.userLevel = userLevel;
        }

        @Override
        public String toString() {
            return "ProfileDto{" +
                    "id=" + id +
                    ", motto='" + motto + '\'' +
                    ", userLevel='" + userLevel + '\'' +
                    '}';
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ImageDto {
        @NotBlank
        private String imageUrl;

        @NotBlank
        private String hashName;

        @Builder
        public ImageDto(String imageUrl, String hashName) {
            this.imageUrl = imageUrl;
            this.hashName = hashName;
        }

        @Override
        public String toString() {
            return "ImageDto{" +
                    "imageUrl='" + imageUrl + '\'' +
                    ", hashName='" + hashName + '\'' +
                    '}';
        }
    }

    @Getter
    @NoArgsConstructor
    public static class HashTagDto {
        @NotBlank
        private String name;

        @Builder
        public HashTagDto(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "HashTagDto{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}