package project.team.ondo.domain.chat.event.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import project.team.ondo.domain.chat.data.payload.ChatRoomListUpdatePayload;
import project.team.ondo.domain.chat.entity.ChatMessageEntity;
import project.team.ondo.domain.chat.event.ChatRoomReadMarkedEvent;
import project.team.ondo.domain.chat.repository.ChatMessageRepository;
import project.team.ondo.domain.chat.service.ChatWsPushService;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatRoomReadWsEventListener {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatWsPushService chatWsPushService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ChatRoomReadMarkedEvent event) {

        long unread = chatMessageRepository.countUnreadMessages(
                event.chatRoomId(),
                event.readerId(),
                event.lastReadMessageId()
        );

        List<ChatMessageEntity> last = chatMessageRepository.findLastMessages(List.of(event.chatRoomId()));
        ChatMessageEntity lastMessage = last.isEmpty() ? null : last.getFirst();

        String preview = lastMessage == null || lastMessage.getContent() == null ? "" : lastMessage.getContent();
        if (preview.length() > 30) preview = preview.substring(0, 30);

        LocalDateTime lastAt = lastMessage == null ? null : lastMessage.getCreatedAt();

        chatWsPushService.pushRoomListUpdateToUser(
                event.readerPublicId(),
                new ChatRoomListUpdatePayload(
                        event.chatRoomPublicId(),
                        unread,
                        preview,
                        lastAt
                )
        );
    }
}
