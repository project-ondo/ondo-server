package project.team.ondo.domain.notification.service;

import project.team.ondo.domain.notification.constant.NotificationType;

import java.util.Map;
import java.util.UUID;

public interface NotificationPushFacade {
    void sendIfAllowed(UUID receiverPublicId, NotificationType type, String title, String body, Map<String, String> data);
}