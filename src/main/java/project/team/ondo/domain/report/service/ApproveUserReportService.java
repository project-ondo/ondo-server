package project.team.ondo.domain.report.service;

public interface ApproveUserReportService {
    void execute(Long reportId, int suspensionDays);
}
