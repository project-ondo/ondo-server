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
import project.team.ondo.domain.chat.service.ChatWsPushService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChatRoomListWsEventListener {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final UserRepository userRepository;

    private final ChatMessageRepository chatMessageRepository;
    private final ChatWsPushService chatWsPushService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ChatMessageSentEvent event) {
        ChatMessageEntity message = event.message();

        ChatRoomEntity chatRoom = chatRoomRepository.findById(message.getRoomId()).orElse(null);
        if (chatRoom == null) return;

        UUID roomPublicId = chatRoom.getPublicId();

        Long userAId = chatRoom.getUserAId();
        Long userBId = chatRoom.getUserBId();

        UserEntity userA = userRepository.findById(userAId).orElse(null);
        UserEntity userB = userRepository.findById(userBId).orElse(null);
        if (userA == null || userB == null) return;

        ChatRoomMemberEntity memberA = chatRoomMemberRepository.findByRoomIdAndUserId(chatRoom.getId(), userAId).orElse(null);
        ChatRoomMemberEntity memberB = chatRoomMemberRepository.findByRoomIdAndUserId(chatRoom.getId(), userBId).orElse(null);

        Long lastReadA = memberA == null ? null : memberA.getLastReadMessageId();
        Long lastReadB = memberB == null ? null : memberB.getLastReadMessageId();

        long unreadA = chatMessageRepository.countUnreadMessages(chatRoom.getId(), userAId, lastReadA);
        long unreadB = chatMessageRepository.countUnreadMessages(chatRoom.getId(), userBId, lastReadB);

        String preview = message.getContent() == null ? "" : message.getContent();
        if (preview.length() > 30) preview = preview.substring(0, 30);

        LocalDateTime lastAt = message.getCreatedAt();

        chatWsPushService.pushRoomListUpdateToUser(
                userA.getPublicId(),
                new ChatRoomListUpdatePayload(roomPublicId, unreadA, preview, lastAt)
        );

        chatWsPushService.pushRoomListUpdateToUser(
                userB.getPublicId(),
                new ChatRoomListUpdatePayload(roomPublicId, unreadB, preview, lastAt)
        );
    }
}
