package faddy.snap.infrastructure.mapper;

import faddy.global.Utils.EncryptionUtils;
import faddy.hashTags.domain.HashTag;
import faddy.image.domain.Image;
import faddy.snap.domain.Snap;
import faddy.snap.domain.dto.response.ThumbnailResponseDto;
import faddy.user.service.UserIdEncryptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ThumbnailMapper {

    private final EncryptionUtils encryptionUtils;
    private final UserIdEncryptionUtil userIdEncryptionUtil;

    /**
     * Snap 엔티티를 ThumbnailResponseDto에 매핑하는 메서드
     *
     * @param snap Snap 엔티티
     * @return ThumbnailResponseDto
     */
    public ThumbnailResponseDto mapSnapToThumbnailResponseDto(Snap snap) throws Exception {

        //snapId 암호화
        String snapId = EncryptionUtils.encryptEntityId(snap.getId());

        //userId 암호화
        String userId = userIdEncryptionUtil.encryptUserId(snap.getUser().getId());

        // 해시태그 이름을 리스트로 추출
        List<String> hashTags = snap.getHashTags().stream()
                .map(HashTag::getName)
                .collect(Collectors.toList());

        // 이미지 URL을 추출 (첫 번째 이미지 사용)
        String imageUrl = snap.getSnapImages().stream()
                .findFirst()
                .map(Image::getImageUrl)
                .orElse("/test_image1.png");

        // 유저 닉네임을 추출 (없으면 Unknown으로 대체)
        String nickname = snap.getUser().getNickname() != null ? snap.getUser().getNickname() : "Unknown";
        System.out.println("snap.getUser().getProfile() = " + snap.getUser().getProfile());
        // 프로필 이미지 URL을 추출 (없으면 기본 이미지로 대체)
        String profileImageUrl = snap.getUser().getProfile().getProfileImageUrl() != null ? snap.getUser().getProfile().getProfileImageUrl() : "/default_profile.jpg";

        // ThumbnailResponseDto 생성
        return new ThumbnailResponseDto(
                snapId,
                userId,
                snap.getDescription() != null ? snap.getDescription() : "No description provided",
                imageUrl,
                hashTags,
                nickname,
                profileImageUrl
        );
    }
}
