package project.team.ondo.domain.report.data.response;

import project.team.ondo.domain.report.constant.ReportStatus;
import project.team.ondo.domain.report.entity.ReportEntity;

public record ReportResponse(Long reportId, ReportStatus status) {

    public static ReportResponse from(ReportEntity entity) {
        return new ReportResponse(entity.getId(), entity.getStatus());
    }
}
