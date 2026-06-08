package project.team.ondo.domain.report.event.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import project.team.ondo.domain.notification.constant.NotificationType;
import project.team.ondo.domain.notification.service.CreateNotificationService;
import project.team.ondo.domain.report.constant.ReportTargetType;
import project.team.ondo.domain.report.event.ReportApprovedEvent;
import project.team.ondo.domain.report.event.ReportCreatedEvent;
import project.team.ondo.domain.report.event.ReportRejectedEvent;
import project.team.ondo.domain.user.entity.UserSuspensionEntity;
import project.team.ondo.domain.user.repository.UserSuspensionRepository;
import project.team.ondo.global.discord.DiscordWebhookService;

@Component
@RequiredArgsConstructor
public class ReportEventListener {

    private final CreateNotificationService createNotificationService;
    private final DiscordWebhookService discordWebhookService;
    private final UserSuspensionRepository userSuspensionRepository;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleReportCreated(ReportCreatedEvent event) {
        discordWebhookService.sendReportNotification(event);

        createNotificationService.create(
                event.reporterPublicId(),
                NotificationType.REPORT_RECEIVED,
                "신고 접수",
                "신고가 접수되었습니다. 검토 후 처리할 예정입니다.",
                "reportId=" + event.reportId()
        );
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleReportApproved(ReportApprovedEvent event) {
        createNotificationService.create(
                event.reporterPublicId(),
                NotificationType.REPORT_RESOLVED,
                "신고 처리 완료",
                "신고가 검토되어 처리되었습니다.",
                "reportId=" + event.reportId()
        );

        if (event.reportedUserPublicId() != null) {
            if (event.targetType() == ReportTargetType.USER) {
                String suspendedUntilMsg = userSuspensionRepository
                        .findByUserPublicId(event.reportedUserPublicId())
                        .map(s -> s.getSuspendedUntil().toString())
                        .orElse("알 수 없음");
                createNotificationService.create(
                        event.reportedUserPublicId(),
                        NotificationType.USER_SUSPENDED,
                        "계정 정지",
                        "운영 정책 위반으로 계정이 " + suspendedUntilMsg + "까지 정지되었습니다.",
                        null
                );
            } else {
                createNotificationService.create(
                        event.reportedUserPublicId(),
                        NotificationType.REPORT_CONTENT_DELETED,
                        "콘텐츠 삭제",
                        "회원님의 콘텐츠가 운영 정책 위반으로 삭제되었습니다.",
                        null
                );
            }
        }
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleReportRejected(ReportRejectedEvent event) {
        createNotificationService.create(
                event.reporterPublicId(),
                NotificationType.REPORT_RESOLVED,
                "신고 처리 완료",
                "신고가 검토되었으나 처리 기준에 해당하지 않아 기각되었습니다.",
                "reportId=" + event.reportId()
        );
    }
}
