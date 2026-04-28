package project.team.ondo.domain.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.chat.constant.OutboxStatus;
import project.team.ondo.domain.chat.data.response.ChatMessageResponse;
import project.team.ondo.domain.chat.entity.ChatMessageOutboxEntity;
import project.team.ondo.domain.chat.repository.ChatMessageOutboxRepository;
import project.team.ondo.domain.chat.repository.ChatMessageRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatOutboxPollerService {

    private final ChatMessageOutboxRepository outboxRepository;
    private final ChatMessageRepository messageRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Scheduled(fixedDelay = 5_000, initialDelay = 10_000)
    @Transactional
    public void pollAndDispatch() {
        LocalDateTime threshold = LocalDateTime.now().minusSeconds(5);
        List<ChatMessageOutboxEntity> pending =
                outboxRepository.findByStatusAndCreatedAtBefore(OutboxStatus.PENDING, threshold);

        for (ChatMessageOutboxEntity outbox : pending) {
            messageRepository.findById(outbox.getMessageId()).ifPresentOrElse(
                    message -> {
                        ChatMessageResponse payload = ChatMessageResponse.from(message, outbox.getRoomPublicId());
                        simpMessagingTemplate.convertAndSend("/topic/chat.rooms." + outbox.getRoomPublicId(), payload);
                        outbox.markDispatched();
                        log.info("Outbox recovery dispatched: messageId={}", outbox.getMessageId());
                    },
                    () -> {
                        outbox.markDispatched();
                        log.warn("Outbox messageId={} not found, marking dispatched", outbox.getMessageId());
                    }
            );
        }
    }

    @Scheduled(fixedDelay = 3_600_000, initialDelay = 60_000)
    @Transactional
    public void cleanup() {
        outboxRepository.deleteByStatusAndProcessedAtBefore(
                OutboxStatus.DISPATCHED,
                LocalDateTime.now().minusHours(24)
        );
    }
}