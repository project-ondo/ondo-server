package project.team.ondo.global.aws.data.response;

import java.time.LocalDateTime;

public record PresignUploadResponse(
        String key,
        String url,
        LocalDateTime expiresAt
) {
}
