package project.team.ondo.domain.report.entity;

import jakarta.persistence.*;
import lombok.*;
import project.team.ondo.domain.report.constant.ReportStatus;
import project.team.ondo.domain.report.constant.ReportTargetType;
import project.team.ondo.global.entity.BaseEntity;

import java.util.UUID;

@Entity
@Table(
        name = "reports",
        indexes = {
                @Index(name = "idx_reports_target", columnList = "target_type, target_id"),
                @Index(name = "idx_reports_reporter", columnList = "reporter_public_id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ReportEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reporter_public_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID reporterPublicId;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false, length = 20)
    private ReportTargetType targetType;

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @Lob
    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReportStatus status;

    public static ReportEntity create(UUID reporterPublicId, ReportTargetType targetType, Long targetId, String description) {
        return ReportEntity.builder()
                .reporterPublicId(reporterPublicId)
                .targetType(targetType)
                .targetId(targetId)
                .description(description)
                .status(ReportStatus.PENDING)
                .build();
    }

    public void approve() {
        this.status = ReportStatus.APPROVED;
    }

    public void reject() {
        this.status = ReportStatus.REJECTED;
    }
}
