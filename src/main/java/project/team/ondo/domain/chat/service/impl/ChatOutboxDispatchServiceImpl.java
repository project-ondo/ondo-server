package project.team.ondo.domain.chat.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.chat.repository.ChatMessageOutboxRepository;
import project.team.ondo.domain.chat.service.ChatOutboxDispatchService;

@Service
@RequiredArgsConstructor
public class ChatOutboxDispatchServiceImpl implements ChatOutboxDispatchService {

    private final ChatMessageOutboxRepository chatMessageOutboxRepository;

    @Transactional
    @Override
    public void markDispatched(Long messageId) {
        chatMessageOutboxRepository.findByMessageId(messageId).ifPresent(outbox -> {
            outbox.markDispatched();
            chatMessageOutboxRepository.save(outbox);
        });
    }
}
