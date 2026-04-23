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
import project.team.ondo.domain.notification.service.NotificationPolicyService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.exception.UserNotFoundException;
import project.team.ondo.domain.user.repository.UserRepository;
import project.team.ondo.global.fcm.data.command.FcmPushCommand;
import project.team.ondo.global.fcm.service.FcmPushService;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MatchNotificationEventListener {

    private final CreateNotificationService createNotificationService;
    private final FcmPushService fcmPushService;
    private final UserRepository userRepository;
    private final NotificationPolicyService notificationPolicyService;

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

        if (!notificationPolicyService.shouldSendPush(event.receiverPublicId(), NotificationType.MATCH_CREATED)) return;

        var data = new HashMap<String, String>();
        data.put("type", "MATCH_CREATED");
        data.put("roomPublicId", event.chatRoomPublicId().toString());
        data.put("opponentPublicId", event.senderPublicId().toString());
        data.put("opponentDisplayName", opponentDisplayName);

        fcmPushService.send(
                new FcmPushCommand(
                        event.receiverPublicId(),
                        title,
                        body,
                        Map.copyOf(data)
                )
        );
    }
}
