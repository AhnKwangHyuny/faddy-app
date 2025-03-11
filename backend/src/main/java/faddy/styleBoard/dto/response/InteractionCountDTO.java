package faddy.styleBoard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InteractionCountDTO {
    private int likeCount;
    private int viewCount;
    private int commentCount;

}
