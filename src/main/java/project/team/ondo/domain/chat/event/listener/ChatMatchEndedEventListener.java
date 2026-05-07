package project.team.ondo.domain.chat.event.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import project.team.ondo.domain.chat.event.ChatMatchEndedEvent;
import project.team.ondo.domain.notification.constant.NotificationType;
import project.team.ondo.domain.notification.service.CreateNotificationService;
import project.team.ondo.domain.notification.service.NotificationPolicyService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.repository.UserRepository;
import project.team.ondo.global.fcm.data.command.FcmPushCommand;
import project.team.ondo.global.fcm.service.FcmPushService;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChatMatchEndedEventListener {

    private final UserRepository userRepository;
    private final CreateNotificationService createNotificationService;
    private final NotificationPolicyService notificationPolicyService;
    private final FcmPushService fcmPushService;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ChatMatchEndedEvent event) {

        UserEntity userA = userRepository.findById(event.userAId()).orElse(null);
        UserEntity userB = userRepository.findById(event.userBId()).orElse(null);
        if (userA == null || userB == null) return;

        saveAndMaybePush(userA.getPublicId(), event.chatRoomPublicId(), userB.getPublicId());

        saveAndMaybePush(userB.getPublicId(), event.chatRoomPublicId(), userA.getPublicId());
    }

    private void saveAndMaybePush(UUID receiverPublicId, UUID roomPublicId, UUID opponentPublicId) {
        NotificationType type = NotificationType.RATE_REQUEST;

        String title = type.getTitle();
        String body = type.getBody();

        String target = "chatRoomPublicId=" + roomPublicId + "&opponentPublicId=" + opponentPublicId;

        createNotificationService.create(receiverPublicId, type, title, body, target);

        if (!notificationPolicyService.shouldSendPush(receiverPublicId, type)) return;

        fcmPushService.send(new FcmPushCommand(
                receiverPublicId,
                title,
                body,
                Map.of(
                        "type", type.name(),
                        "chatRoomPublicId", roomPublicId.toString(),
                        "opponentPublicId", opponentPublicId.toString()
                )
        ));
    }
}
