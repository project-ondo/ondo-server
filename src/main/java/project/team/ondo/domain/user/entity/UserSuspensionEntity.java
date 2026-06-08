package project.team.ondo.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import project.team.ondo.global.jpa.UuidBinaryConverter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "user_suspensions",
        uniqueConstraints = @UniqueConstraint(name = "uq_user_suspensions_user", columnNames = "user_public_id")
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserSuspensionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = UuidBinaryConverter.class)
    @Column(name = "user_public_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID userPublicId;

    @Column(name = "report_id", nullable = false)
    private Long reportId;

    @Column(name = "suspended_at", nullable = false)
    private LocalDateTime suspendedAt;

    @Column(name = "suspended_until", nullable = false)
    private LocalDateTime suspendedUntil;

    public static UserSuspensionEntity create(UUID userPublicId, Long reportId, int suspensionDays) {
        LocalDateTime now = LocalDateTime.now();
        return UserSuspensionEntity.builder()
                .userPublicId(userPublicId)
                .reportId(reportId)
                .suspendedAt(now)
                .suspendedUntil(now.plusDays(suspensionDays))
                .build();
    }

    public void update(Long reportId, int suspensionDays) {
        LocalDateTime now = LocalDateTime.now();
        this.reportId = reportId;
        this.suspendedAt = now;
        this.suspendedUntil = this.suspendedUntil != null && this.suspendedUntil.isAfter(now)
                ? this.suspendedUntil.plusDays(suspensionDays)
                : now.plusDays(suspensionDays);
    }
}
