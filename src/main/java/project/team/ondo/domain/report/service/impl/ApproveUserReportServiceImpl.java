package project.team.ondo.domain.report.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.report.constant.ReportStatus;
import project.team.ondo.domain.report.constant.ReportTargetType;
import project.team.ondo.domain.report.entity.ReportEntity;
import project.team.ondo.domain.report.event.ReportApprovedEvent;
import project.team.ondo.domain.report.exception.ReportAlreadyProcessedException;
import project.team.ondo.domain.report.exception.ReportTargetNotFoundException;
import project.team.ondo.domain.report.repository.ReportRepository;
import project.team.ondo.domain.report.service.ApproveUserReportService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.entity.UserSuspensionEntity;
import project.team.ondo.domain.user.repository.UserRepository;
import project.team.ondo.domain.user.repository.UserSuspensionRepository;

@Service
@RequiredArgsConstructor
public class ApproveUserReportServiceImpl implements ApproveUserReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final UserSuspensionRepository userSuspensionRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public void execute(Long reportId, int suspensionDays) {
        ReportEntity report = reportRepository.getByIdOrThrow(reportId);

        if (report.getStatus() != ReportStatus.PENDING) {
            throw new ReportAlreadyProcessedException();
        }

        UserEntity targetUser = userRepository.findById(report.getTargetId())
                .orElseThrow(ReportTargetNotFoundException::new);

        userSuspensionRepository.findByUserPublicId(targetUser.getPublicId())
                .ifPresentOrElse(
                        suspension -> suspension.update(reportId, suspensionDays),
                        () -> userSuspensionRepository.save(UserSuspensionEntity.create(
                                targetUser.getPublicId(), reportId, suspensionDays))
                );

        eventPublisher.publishEvent(new ReportApprovedEvent(
                reportId,
                report.getReporterPublicId(),
                targetUser.getPublicId(),
                ReportTargetType.USER,
                report.getTargetId()
        ));

        reportRepository.updateStatusByTargetTypeAndTargetId(ReportTargetType.USER, report.getTargetId(), ReportStatus.APPROVED);
    }
}