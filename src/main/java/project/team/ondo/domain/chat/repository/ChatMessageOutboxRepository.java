package project.team.ondo.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.chat.constant.OutboxStatus;
import project.team.ondo.domain.chat.entity.ChatMessageOutboxEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatMessageOutboxRepository extends JpaRepository<ChatMessageOutboxEntity, Long> {

    Optional<ChatMessageOutboxEntity> findByMessageId(Long messageId);

    List<ChatMessageOutboxEntity> findByStatusAndCreatedAtBefore(OutboxStatus status, LocalDateTime threshold);

    void deleteByStatusAndProcessedAtBefore(OutboxStatus status, LocalDateTime threshold);

    @Modifying
    @Query("DELETE FROM ChatMessageOutboxEntity o WHERE o.roomPublicId = :roomPublicId")
    void deleteAllByRoomPublicId(@Param("roomPublicId") UUID roomPublicId);
}
