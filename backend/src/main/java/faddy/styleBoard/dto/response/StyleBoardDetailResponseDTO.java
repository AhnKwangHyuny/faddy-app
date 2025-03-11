package faddy.styleBoard.dto.response;

import faddy.profile.domain.UserLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class StyleBoardDetailResponseDTO {

    private final String title;
    private final String content;
    private final String category;
    private final LocalDateTime createdAt;

    private final String nickname;
    private final UserLevel userLevel;
    private final String profileImageUrl;

    private final int likeCount;
    private final boolean isLiked;

    private int viewCount;

    private final List<String> hashTags;

}
