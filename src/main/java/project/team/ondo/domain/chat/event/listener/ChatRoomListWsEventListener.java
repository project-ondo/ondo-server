package project.team.ondo.domain.chat.event.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import project.team.ondo.domain.chat.data.payload.ChatRoomListUpdatePayload;
import project.team.ondo.domain.chat.entity.ChatMessageEntity;
import project.team.ondo.domain.chat.entity.ChatRoomEntity;
import project.team.ondo.domain.chat.entity.ChatRoomMemberEntity;
import project.team.ondo.domain.chat.event.ChatMessageSentEvent;
import project.team.ondo.domain.chat.repository.ChatMessageRepository;
import project.team.ondo.domain.chat.repository.ChatRoomMemberRepository;
import project.team.ondo.domain.chat.repository.ChatRoomRepository;
import project.team.ondo.domain.chat.service.ChatPresenceService;
import project.team.ondo.domain.chat.service.ChatWsPushService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ChatRoomListWsEventListener {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final UserRepository userRepository;

    private final ChatMessageRepository chatMessageRepository;
    private final ChatWsPushService chatWsPushService;
    private final ChatPresenceService chatPresenceService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ChatMessageSentEvent event) {
        ChatMessageEntity message = event.message();

        ChatRoomEntity chatRoom = chatRoomRepository.findById(message.getRoomId()).orElse(null);
        if (chatRoom == null) return;

        UUID roomPublicId = chatRoom.getPublicId();

        Long userAId = chatRoom.getUserAId();
        Long userBId = chatRoom.getUserBId();

        Long senderId = message.getSenderId();
        Long receiverId = userAId.equals(senderId) ? userBId : userAId;

        Map<Long, UserEntity> userMap = userRepository.findAllById(List.of(userAId, userBId)).stream()
                .collect(Collectors.toMap(UserEntity::getId, Function.identity()));

        UserEntity userA = userMap.get(userAId);
        UserEntity userB = userMap.get(userBId);
        if (userA == null || userB == null) return;

        UUID userAPublicId = userA.getPublicId();
        UUID userBPublicId = userB.getPublicId();

        String preview = message.getContent() == null ? "" : message.getContent();
        if (preview.length() > 30) preview = preview.substring(0, 30);

        LocalDateTime lastAt = message.getCreatedAt();

        long unreadSender = 0L;

        UUID receiverPublicId = receiverId.equals(userAId) ? userAPublicId : userBPublicId;

        long unreadReceiver;
        if (chatPresenceService.isViewingRoom(receiverPublicId, roomPublicId)) {
            unreadReceiver = 0L;
        } else {
            ChatRoomMemberEntity receiverMember = chatRoomMemberRepository
                    .findByRoomIdAndUserId(chatRoom.getId(), receiverId)
                    .orElse(null);

            Long lastRead = receiverMember == null ? null : receiverMember.getLastReadMessageId();

            unreadReceiver = chatMessageRepository.countUnreadMessages(
                    chatRoom.getId(),
                    receiverId,
                    lastRead
            );
        }

        long unreadA = userAId.equals(senderId) ? unreadSender : unreadReceiver;
        long unreadB = userBId.equals(senderId) ? unreadSender : unreadReceiver;

        chatWsPushService.pushRoomListUpdateToUser(
                userAPublicId,
                new ChatRoomListUpdatePayload(roomPublicId, unreadA, preview, lastAt)
        );

        chatWsPushService.pushRoomListUpdateToUser(
                userBPublicId,
                new ChatRoomListUpdatePayload(roomPublicId, unreadB, preview, lastAt)
        );
    }
}
