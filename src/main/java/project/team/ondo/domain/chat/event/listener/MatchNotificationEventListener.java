package project.team.ondo.domain.chat.event.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import project.team.ondo.domain.chat.event.ChatRoomMatchedEvent;
import project.team.ondo.domain.notification.constant.NotificationType;
import project.team.ondo.domain.notification.service.CreateNotificationService;
import project.team.ondo.domain.notification.service.NotificationPushFacade;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.exception.UserNotFoundException;
import project.team.ondo.domain.user.repository.UserRepository;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class MatchNotificationEventListener {

    private final CreateNotificationService createNotificationService;
    private final UserRepository userRepository;
    private final NotificationPushFacade notificationPushFacade;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ChatRoomMatchedEvent event) {
        UserEntity opponent = userRepository.findByPublicId(event.senderPublicId()).orElseThrow(UserNotFoundException::new);

        String opponentDisplayName = opponent.getDisplayName();
        String title = "새로운 매칭이 성사되었습니다!";
        String body = "지금 바로" + opponentDisplayName + "님과 채팅방에서 대화를 시작해보세요.";

        createNotificationService.create(
                event.receiverPublicId(),
                NotificationType.MATCH_CREATED,
                title,
                body,
                "chatRoomPublicId=" + event.chatRoomPublicId()
        );

        notificationPushFacade.sendIfAllowed(
                event.receiverPublicId(),
                NotificationType.MATCH_CREATED,
                title,
                body,
                Map.of(
                        "type", "MATCH_CREATED",
                        "roomPublicId", event.chatRoomPublicId().toString(),
                        "opponentPublicId", event.senderPublicId().toString(),
                        "opponentDisplayName", opponentDisplayName
                )
        );
    }
}
