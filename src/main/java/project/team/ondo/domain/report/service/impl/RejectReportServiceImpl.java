package project.team.ondo.domain.report.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.report.entity.ReportEntity;
import project.team.ondo.domain.report.event.ReportRejectedEvent;
import project.team.ondo.domain.report.repository.ReportRepository;
import project.team.ondo.domain.report.service.RejectReportService;

@Service
@RequiredArgsConstructor
public class RejectReportServiceImpl implements RejectReportService {

    private final ReportRepository reportRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public void execute(Long reportId) {
        ReportEntity report = reportRepository.getByIdOrThrow(reportId);
        report.reject();

        eventPublisher.publishEvent(new ReportRejectedEvent(
                reportId,
                report.getReporterPublicId()
        ));
    }
}
