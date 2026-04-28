package project.team.ondo.domain.chat.entity;

import jakarta.persistence.*;
import lombok.*;
import project.team.ondo.domain.chat.constant.OutboxStatus;
import project.team.ondo.global.entity.BaseEntity;
import project.team.ondo.global.jpa.UuidBinaryConverter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "chat_message_outbox", indexes = {
        @Index(name = "idx_outbox_status_created", columnList = "status, created_at")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatMessageOutboxEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message_id", nullable = false)
    private Long messageId;

    @Convert(converter = UuidBinaryConverter.class)
    @Column(name = "room_public_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID roomPublicId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OutboxStatus status;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    public static ChatMessageOutboxEntity create(Long messageId, UUID roomPublicId) {
        return ChatMessageOutboxEntity.builder()
                .messageId(messageId)
                .roomPublicId(roomPublicId)
                .status(OutboxStatus.PENDING)
                .build();
    }

    public void markDispatched() {
        this.status = OutboxStatus.DISPATCHED;
        this.processedAt = LocalDateTime.now();
    }
}