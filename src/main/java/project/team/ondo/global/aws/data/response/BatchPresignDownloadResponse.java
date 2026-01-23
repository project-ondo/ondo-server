package project.team.ondo.global.aws.data.response;

import java.time.LocalDateTime;
import java.util.List;

public record BatchPresignDownloadResponse(
        List<BatchPresignDownloadItem> items
) {
    public record BatchPresignDownloadItem(
            String key,
            String url,
            LocalDateTime expiresAt
    ) {}
}
