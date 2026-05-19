package project.team.ondo.domain.notification.data.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;

public record UpdateMyNotificationSettingRequest(
        Boolean pushEnabled,
        Boolean chatEnabled,
        Boolean communityEnabled,
        Boolean dndEnabled,
        @Schema(description = "방해 금지 시작 시간 (HH:mm:ss)", example = "22:00:00")
        LocalTime dndStart,
        @Schema(description = "방해 금지 종료 시간 (HH:mm:ss)", example = "08:00:00")
        LocalTime dndEnd
) {
}
