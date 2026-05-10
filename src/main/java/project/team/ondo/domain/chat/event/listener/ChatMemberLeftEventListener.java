package project.team.ondo.domain.chat.event.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import project.team.ondo.domain.chat.event.ChatMemberLeftEvent;
import project.team.ondo.domain.notification.constant.NotificationType;
import project.team.ondo.domain.notification.service.CreateNotificationService;
import project.team.ondo.domain.notification.service.NotificationPushFacade;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.repository.UserRepository;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChatMemberLeftEventListener {

    private final UserRepository userRepository;
    private final CreateNotificationService createNotificationService;
    private final NotificationPushFacade notificationPushFacade;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ChatMemberLeftEvent event) {
        UserEntity leaver = userRepository.findById(event.leftUserId()).orElse(null);
        UserEntity opponent = userRepository.findById(event.opponentUserId()).orElse(null);
        if (leaver == null || opponent == null) return;

        if (event.hadMessages()) {
            sendRateRequest(leaver.getPublicId(), event.roomPublicId(), opponent.getPublicId());
            sendRateRequest(opponent.getPublicId(), event.roomPublicId(), leaver.getPublicId());
        } else {
            notifyPartnerLeft(opponent.getPublicId(), event.roomPublicId());
        }
    }

    private void sendRateRequest(UUID receiverPublicId, UUID roomPublicId, UUID opponentPublicId) {
        NotificationType type = NotificationType.RATE_REQUEST;
        String target = "chatRoomPublicId=" + roomPublicId + "&opponentPublicId=" + opponentPublicId;

        createNotificationService.create(receiverPublicId, type, type.getTitle(), type.getBody(), target);

        notificationPushFacade.sendIfAllowed(
                receiverPublicId,
                type,
                type.getTitle(),
                type.getBody(),
                Map.of(
                        "type", type.name(),
                        "chatRoomPublicId", roomPublicId.toString(),
                        "opponentPublicId", opponentPublicId.toString()
                )
        );
    }

    private void notifyPartnerLeft(UUID receiverPublicId, UUID roomPublicId) {
        NotificationType type = NotificationType.CHAT_MEMBER_LEFT;
        String target = "chatRoomPublicId=" + roomPublicId;

        createNotificationService.create(receiverPublicId, type, type.getTitle(), type.getBody(), target);

        notificationPushFacade.sendIfAllowed(
                receiverPublicId,
                type,
                type.getTitle(),
                type.getBody(),
                Map.of(
                        "type", type.name(),
                        "chatRoomPublicId", roomPublicId.toString()
                )
        );
    }
}
