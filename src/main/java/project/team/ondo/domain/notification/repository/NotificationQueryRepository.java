package project.team.ondo.domain.notification.repository;

import java.util.UUID;

public interface NotificationQueryRepository {
    long markAllRead(UUID receiverPublicId);
}
