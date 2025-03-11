package faddy.snap.domain.projection;

import faddy.hashTags.domain.projection.HashTagProjection;
import faddy.image.domain.projection.ImageProjection;
import faddy.user.domain.projection.UserProjection;

import java.time.LocalDateTime;
import java.util.List;

public interface SnapProjection {
    String getDescription();
    LocalDateTime getCreated_at();
    UserProjection getUser();
    List<ImageProjection> getImages();
    List<HashTagProjection> getHashTags();
}
