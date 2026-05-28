package project.team.ondo.domain.report.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.chat.entity.ChatRoomEntity;
import project.team.ondo.domain.chat.repository.ChatRoomRepository;
import project.team.ondo.domain.community.comment.constant.CommentStatus;
import project.team.ondo.domain.community.comment.entity.CommentEntity;
import project.team.ondo.domain.community.comment.repository.CommentRepository;
import project.team.ondo.domain.community.post.constant.PostStatus;
import project.team.ondo.domain.community.post.entity.PostEntity;
import project.team.ondo.domain.community.post.repository.PostRepository;
import project.team.ondo.domain.report.entity.ReportEntity;
import project.team.ondo.domain.report.event.ReportApprovedEvent;
import project.team.ondo.domain.report.repository.ReportRepository;
import project.team.ondo.domain.report.service.ApproveReportService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApproveReportServiceImpl implements ApproveReportService {

    private final ReportRepository reportRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public void execute(Long reportId) {
        ReportEntity report = reportRepository.getByIdOrThrow(reportId);

        UUID reportedUserPublicId = resolveReportedUser(report);

        // Publish event before deletion so listeners carry all necessary data
        eventPublisher.publishEvent(new ReportApprovedEvent(
                reportId,
                report.getReporterPublicId(),
                reportedUserPublicId,
                report.getTargetType(),
                report.getTargetId()
        ));

        deleteContent(report);

        reportRepository.deleteAllByTargetTypeAndTargetId(report.getTargetType(), report.getTargetId());
    }

    private UUID resolveReportedUser(ReportEntity report) {
        return switch (report.getTargetType()) {
            case POST -> postRepository.findByIdAndStatus(report.getTargetId(), PostStatus.ACTIVE)
                    .map(p -> p.getAuthor().getPublicId())
                    .orElse(null);
            case COMMENT -> commentRepository.findByIdAndStatus(report.getTargetId(), CommentStatus.ACTIVE)
                    .map(c -> c.getAuthor().getPublicId())
                    .orElse(null);
            case CHAT_ROOM -> null;
        };
    }

    private void deleteContent(ReportEntity report) {
        switch (report.getTargetType()) {
            case POST -> postRepository.findByIdAndStatus(report.getTargetId(), PostStatus.ACTIVE)
                    .ifPresent(PostEntity::delete);
            case COMMENT -> commentRepository.findByIdAndStatus(report.getTargetId(), CommentStatus.ACTIVE)
                    .ifPresent(CommentEntity::delete);
            case CHAT_ROOM -> chatRoomRepository.findById(report.getTargetId())
                    .filter(r -> !r.isEnded())
                    .ifPresent(ChatRoomEntity::end);
        }
    }
}
