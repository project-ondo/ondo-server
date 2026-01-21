package project.team.ondo.domain.notification.data.request;

import java.time.LocalTime;

public record UpdateMyNotificationSettingRequest(
        Boolean pushEnabled,
        Boolean chatEnabled,
        Boolean communityEnabled,
        Boolean dndEnabled,
        LocalTime dndStart,
        LocalTime dndEnd
) {
}
