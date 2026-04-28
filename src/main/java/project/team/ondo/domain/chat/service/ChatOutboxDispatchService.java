package project.team.ondo.domain.chat.service;

public interface ChatOutboxDispatchService {
    void markDispatched(Long messageId);
}