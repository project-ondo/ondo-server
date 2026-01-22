package project.team.ondo.domain.notification.entity;

import jakarta.persistence.*;
import lombok.*;
import project.team.ondo.domain.notification.constant.NotificationType;
import project.team.ondo.global.entity.BaseEntity;

import java.util.UUID;

@Entity
@Table(
        name = "notifications",
        indexes = {
                @Index(name = "idx_notifications_receiver_created", columnList = "receiver_public_id, created_at"),
                @Index(name = "idx_notifications_receiver_read_created", columnList = "receiver_public_id, `read`, created_at")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NotificationEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "receiver_public_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID receiverPublicId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NotificationType type;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 500)
    private String body;

    @Column(length = 200)
    private String target;

    @Builder.Default
    @Column(nullable = false, name = "`read`")
    private boolean read = false;

    @Builder.Default
    @Column(name = "group_count", nullable = false)
    private long groupCount = 1L;

    public static NotificationEntity create(
            UUID receiverPublicId,
            NotificationType type,
            String title,
            String body
    ) {
        return NotificationEntity.builder()
                .receiverPublicId(receiverPublicId)
                .type(type)
                .title(title)
                .body(body)
                .read(false)
                .build();
    }

    public static NotificationEntity create(
            UUID receiverPublicId,
            NotificationType type,
            String title,
            String body,
            String target
    ) {
        return NotificationEntity.builder()
                .receiverPublicId(receiverPublicId)
                .type(type)
                .title(title)
                .body(body)
                .target(target)
                .read(false)
                .build();
    }

    public void increaseGroupCount() {
        this.groupCount++;
    }

    public void markRead() {
        this.read = true;
    }

    public void updateBody(String body) {
        this.body = body;
    }

    public void updateTitle(String title) {
        this.title = title;
    }
}
