package project.team.ondo.domain.chat.event.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import project.team.ondo.domain.chat.data.payload.ChatRoomListUpdatePayload;
import project.team.ondo.domain.chat.entity.ChatMessageEntity;
import project.team.ondo.domain.chat.constant.ChatConstants;
import project.team.ondo.domain.chat.event.ChatRoomReadMarkedEvent;
import project.team.ondo.domain.chat.repository.ChatMessageRepository;
import project.team.ondo.domain.chat.service.ChatWsPushService;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ChatRoomReadWsEventListener {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatWsPushService chatWsPushService;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ChatRoomReadMarkedEvent event) {

        long unread = 0L;

        ChatMessageEntity lastMessage = chatMessageRepository
                .findTopByRoomIdOrderByIdDesc(event.chatRoomId())
                .orElse(null);

        String preview = (lastMessage == null || lastMessage.getContent() == null)
                ? ""
                : lastMessage.getContent();

        if (preview.length() > ChatConstants.PREVIEW_MAX_LENGTH) preview = preview.substring(0, ChatConstants.PREVIEW_MAX_LENGTH);

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
