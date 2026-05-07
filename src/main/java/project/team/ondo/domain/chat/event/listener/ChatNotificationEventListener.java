package project.team.ondo.domain.chat.event.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import project.team.ondo.domain.chat.entity.ChatMessageEntity;
import project.team.ondo.domain.chat.entity.ChatRoomEntity;
import project.team.ondo.domain.chat.event.ChatMessageSentEvent;
import project.team.ondo.domain.chat.exception.ChatRoomNotFoundException;
import project.team.ondo.domain.chat.repository.ChatRoomMuteCommandRepository;
import project.team.ondo.domain.chat.repository.ChatRoomRepository;
import project.team.ondo.domain.chat.service.ChatPresenceService;
import project.team.ondo.domain.notification.constant.NotificationType;
import project.team.ondo.domain.notification.service.CreateNotificationService;
import project.team.ondo.domain.notification.service.NotificationPushFacade;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.exception.UserNotFoundException;
import project.team.ondo.domain.user.repository.UserRepository;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChatNotificationEventListener {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatPresenceService chatPresenceService;
    private final CreateNotificationService createNotificationService;
    private final ChatRoomMuteCommandRepository chatRoomMuteRepository;
    private final NotificationPushFacade notificationPushFacade;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ChatMessageSentEvent event) {
        ChatMessageEntity message = event.message();

        ChatRoomEntity chatRoom = chatRoomRepository.findById(message.getRoomId()).orElseThrow(ChatRoomNotFoundException::new);

        UUID roomPublicId = chatRoom.getPublicId();

        Long receiverId = chatRoom.getUserAId().equals(message.getSenderId())
                ? chatRoom.getUserBId()
                : chatRoom.getUserAId();

        UserEntity receiver = userRepository.findById(receiverId).orElseThrow(UserNotFoundException::new);

        if (chatPresenceService.isViewingRoom(receiver.getPublicId(), roomPublicId)) {
            return;
        }

        if (chatRoomMuteRepository.isMuted(roomPublicId, receiverId)) {
            return;
        }

        String body = switch (message.getMessageType()) {
            case TEXT -> message.getContent();
            default -> "새 메시지가 도착했습니다.";
        };

        createNotificationService.create(
                receiver.getPublicId(),
                NotificationType.CHAT_MESSAGE,
                "새 메시지",
                body,
                "chatRoomPublicId=" + roomPublicId
        );

        notificationPushFacade.sendIfAllowed(
                receiver.getPublicId(),
                NotificationType.CHAT_MESSAGE,
                "새 메시지",
                body,
                Map.of(
                        "type", "CHAT_MESSAGE",
                        "roomPublicId", roomPublicId.toString()
                )
        );
    }
}
