package faddy.image.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ImageLookupRequestDTO(
        @NotBlank(message = "이미지 URL은 필수입니다.")
        String imageUrl
) {}
