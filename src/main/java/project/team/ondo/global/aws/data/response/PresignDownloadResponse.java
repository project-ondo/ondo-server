package project.team.ondo.global.aws.data.response;

import java.time.LocalDateTime;

public record PresignDownloadResponse(
        String getUrl,
        LocalDateTime expiresAt
) {
}
