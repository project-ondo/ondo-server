package project.team.ondo.domain.notification.service;

import project.team.ondo.domain.notification.constant.NotificationType;
import project.team.ondo.domain.notification.data.NotificationResult;

import java.util.UUID;

public interface CreateNotificationService {

    void create(UUID receiverPublicId, NotificationType type, String title, String body, String target);

    NotificationResult createOrAggregate(UUID receiverPublicId, NotificationType type, String target,
                                         String title, String singleBody, String aggregatedBodyPrefix);
}
