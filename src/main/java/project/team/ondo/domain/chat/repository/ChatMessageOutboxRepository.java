package project.team.ondo.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.chat.constant.OutboxStatus;
import project.team.ondo.domain.chat.entity.ChatMessageOutboxEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageOutboxRepository extends JpaRepository<ChatMessageOutboxEntity, Long> {

    Optional<ChatMessageOutboxEntity> findByMessageId(Long messageId);

    List<ChatMessageOutboxEntity> findByStatusAndCreatedAtBefore(OutboxStatus status, LocalDateTime threshold);

    void deleteByStatusAndProcessedAtBefore(OutboxStatus status, LocalDateTime threshold);
}
