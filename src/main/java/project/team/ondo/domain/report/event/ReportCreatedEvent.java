package project.team.ondo.domain.report.event;

import project.team.ondo.domain.report.constant.ReportTargetType;

import java.util.UUID;

public record ReportCreatedEvent(
        Long reportId,
        UUID reporterPublicId,
        ReportTargetType targetType,
        Long targetId,
        String description,
        String contentSnapshot
) {}
