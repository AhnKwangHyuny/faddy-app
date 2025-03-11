package faddy.chat.dto.request;

import lombok.Builder;

@Builder
public record AuthorizationTokenRequest(String token) {
}
