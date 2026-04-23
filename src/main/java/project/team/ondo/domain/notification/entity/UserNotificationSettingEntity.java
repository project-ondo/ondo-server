package project.team.ondo.domain.notification.entity;

import jakarta.persistence.*;
import lombok.*;
import project.team.ondo.global.entity.BaseEntity;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "user_notification_setting",
        uniqueConstraints = @UniqueConstraint(name = "uk_user_notification_setting_user", columnNames = "user_public_id"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserNotificationSettingEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_public_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID userPublicId;

    @Builder.Default
    @Column(name = "push_enabled", nullable = false)
    private boolean pushEnabled = true;

    @Builder.Default
    @Column(name = "chat_enabled", nullable = false)
    private boolean chatEnabled = true;

    @Builder.Default
    @Column(name = "community_enabled", nullable = false)
    private boolean communityEnabled = true;

    @Builder.Default
    @Column(name = "dnd_enabled", nullable = false)
    private boolean dndEnabled = false;

    @Column(name = "dnd_start")
    private LocalTime dndStart;

    @Column(name = "dnd_end")
    private LocalTime dndEnd;

    public static UserNotificationSettingEntity defaultOf(UUID userPublicId) {
        return UserNotificationSettingEntity.builder()
                .userPublicId(userPublicId)
                .pushEnabled(true)
                .chatEnabled(true)
                .communityEnabled(true)
                .dndEnabled(false)
                .build();
    }

    public void update(Boolean pushEnabled, Boolean chatEnabled, Boolean communityEnabled,
                       Boolean dndEnabled, LocalTime dndStart, LocalTime dndEnd) {
        if (pushEnabled != null) this.pushEnabled = pushEnabled;
        if (chatEnabled != null) this.chatEnabled = chatEnabled;
        if (communityEnabled != null) this.communityEnabled = communityEnabled;
        if (dndEnabled != null) this.dndEnabled = dndEnabled;
        if (dndStart != null) this.dndStart = dndStart;
        if (dndEnd != null) this.dndEnd = dndEnd;
    }
}
