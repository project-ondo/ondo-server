package project.team.ondo.domain.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.chat.constant.OutboxStatus;
import project.team.ondo.domain.chat.data.response.ChatMessageResponse;
import project.team.ondo.domain.chat.entity.ChatMessageEntity;
import project.team.ondo.domain.chat.entity.ChatMessageOutboxEntity;
import project.team.ondo.domain.chat.repository.ChatMessageOutboxRepository;
import project.team.ondo.domain.chat.repository.ChatMessageRepository;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatOutboxPollerService {

    private final ChatMessageOutboxRepository outboxRepository;
    private final ChatMessageRepository messageRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserRepository userRepository;

    @Scheduled(fixedDelay = 5_000, initialDelay = 10_000)
    @Transactional
    public void pollAndDispatch() {
        LocalDateTime threshold = LocalDateTime.now().minusSeconds(5);
        List<ChatMessageOutboxEntity> pending =
                outboxRepository.findByStatusAndCreatedAtBefore(OutboxStatus.PENDING, threshold);

        List<Long> messageIds = pending.stream().map(ChatMessageOutboxEntity::getMessageId).toList();
        Map<Long, ChatMessageEntity> messageMap = messageRepository.findAllById(messageIds)
                .stream().collect(Collectors.toMap(ChatMessageEntity::getId, m -> m));

        List<Long> senderIds = messageMap.values().stream().map(ChatMessageEntity::getSenderId).distinct().toList();
        Map<Long, UserEntity> senderMap = userRepository.findAllById(senderIds)
                .stream().collect(Collectors.toMap(UserEntity::getId, u -> u));

        for (ChatMessageOutboxEntity outbox : pending) {
            ChatMessageEntity message = messageMap.get(outbox.getMessageId());
            if (message == null) {
                outbox.markDispatched();
                log.warn("Outbox messageId={} not found, marking dispatched", outbox.getMessageId());
                continue;
            }
            UserEntity sender = senderMap.get(message.getSenderId());
            if (sender == null) {
                outbox.markDispatched();
                log.warn("Outbox messageId={} sender not found, marking dispatched", outbox.getMessageId());
                continue;
            }
            ChatMessageResponse payload = ChatMessageResponse.from(message, outbox.getRoomPublicId(), sender);
            simpMessagingTemplate.convertAndSend("/topic/chat.rooms." + outbox.getRoomPublicId(), payload);
            outbox.markDispatched();
            log.info("Outbox recovery dispatched: messageId={}", outbox.getMessageId());
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