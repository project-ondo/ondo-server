package project.team.ondo.global.aws.data.response;

import java.time.LocalDateTime;

public record PresignGetResponse(
        String url,
        LocalDateTime expiresAt
) {
}
