package project.team.ondo.domain.notification.data.response;

import project.team.ondo.domain.notification.entity.UserNotificationSettingEntity;

import java.time.LocalTime;

public record MyNotificationSettingResponse(
        boolean pushEnabled,
        boolean chatEnabled,
        boolean communityEnabled,
        boolean dndEnabled,
        LocalTime dndStart,
        LocalTime dndEnd
) {
    public static MyNotificationSettingResponse from(UserNotificationSettingEntity e) {
        return new MyNotificationSettingResponse(
                e.isPushEnabled(),
                e.isChatEnabled(),
                e.isCommunityEnabled(),
                e.isDndEnabled(),
                e.getDndStart(),
                e.getDndEnd()
        );
    }
}
