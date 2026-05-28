package project.team.ondo.domain.report.event;

import project.team.ondo.domain.report.constant.ReportTargetType;

import java.util.UUID;

public record ReportApprovedEvent(
        Long reportId,
        UUID reporterPublicId,
        UUID reportedUserPublicId,
        ReportTargetType targetType,
        Long targetId
) {}
