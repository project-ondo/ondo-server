package project.team.ondo.domain.notification.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.team.ondo.domain.notification.constant.NotificationType;
import project.team.ondo.domain.notification.service.NotificationPushFacade;
import project.team.ondo.domain.notification.service.NotificationPolicyService;
import project.team.ondo.global.fcm.data.command.FcmPushCommand;
import project.team.ondo.global.fcm.service.FcmPushService;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationPushFacadeImpl implements NotificationPushFacade {

    private final NotificationPolicyService notificationPolicyService;
    private final FcmPushService fcmPushService;

    @Override
    public void sendIfAllowed(UUID receiverPublicId, NotificationType type, String title, String body, Map<String, String> data) {
        if (!notificationPolicyService.shouldSendPush(receiverPublicId, type)) return;
        fcmPushService.send(new FcmPushCommand(receiverPublicId, title, body, data));
    }
}
