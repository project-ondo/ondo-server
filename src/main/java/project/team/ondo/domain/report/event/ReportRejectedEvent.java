package project.team.ondo.domain.report.event;

import java.util.UUID;

public record ReportRejectedEvent(
        Long reportId,
        UUID reporterPublicId
) {}
