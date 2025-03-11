package faddy.user.dto.response;

import lombok.Data;

@Data
public class VerificationResultDto {

    private final boolean result;

    public VerificationResultDto(boolean result) {
        this.result = result;
    }
}
