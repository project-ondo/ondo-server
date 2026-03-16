package project.team.ondo.domain.notification.repository;

import java.util.UUID;

public interface NotificationCommandRepository {
    long markAllRead(UUID receiverPublicId);
}
