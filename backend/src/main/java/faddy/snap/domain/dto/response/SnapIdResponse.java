package faddy.snap.domain.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SnapIdResponse {

    private String snapToken;

    public SnapIdResponse(String snapToken) {
        this.snapToken = snapToken;
    }
}
