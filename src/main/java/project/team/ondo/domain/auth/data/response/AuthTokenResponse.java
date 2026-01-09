package project.team.ondo.domain.auth.data.response;

import java.time.LocalDateTime;

public record AuthTokenResponse(
        String accessToken,
        LocalDateTime accessTokenExpiration,
        String refreshToken,
        LocalDateTime refreshTokenExpiration
) {
}
