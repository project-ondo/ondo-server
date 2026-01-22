package project.team.ondo.domain.notification.data.response;

import project.team.ondo.domain.notification.constant.NotificationType;
import project.team.ondo.domain.notification.entity.NotificationEntity;

import java.time.LocalDateTime;

public record NotificationItemResponse(
        Long id,
        NotificationType type,
        String title,
        String body,
        String target,
        boolean read,
        LocalDateTime createdAt
) {
    public static NotificationItemResponse from(NotificationEntity notification) {
        return new NotificationItemResponse(
                notification.getId(),
                notification.getType(),
                notification.getTitle(),
                notification.getBody(),
                notification.getTarget(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}
