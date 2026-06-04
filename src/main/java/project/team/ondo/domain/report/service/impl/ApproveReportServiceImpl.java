package project.team.ondo.domain.report.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.chat.repository.ChatMessageOutboxRepository;
import project.team.ondo.domain.chat.repository.ChatMessageRepository;
import project.team.ondo.domain.chat.repository.ChatRoomMemberRepository;
import project.team.ondo.domain.chat.repository.ChatRoomRepository;
import project.team.ondo.domain.community.comment.constant.CommentStatus;
import project.team.ondo.domain.community.comment.entity.CommentEntity;
import project.team.ondo.domain.community.comment.repository.CommentRepository;
import project.team.ondo.domain.community.post.constant.PostStatus;
import project.team.ondo.domain.community.post.entity.PostEntity;
import project.team.ondo.domain.community.post.repository.PostRepository;
import project.team.ondo.domain.report.constant.ReportStatus;
import project.team.ondo.domain.report.entity.ReportEntity;
import project.team.ondo.domain.report.event.ReportApprovedEvent;
import project.team.ondo.domain.report.exception.ReportAlreadyProcessedException;
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
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageOutboxRepository chatMessageOutboxRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public void execute(Long reportId) {
        ReportEntity report = reportRepository.getByIdOrThrow(reportId);

        if (report.getStatus() != ReportStatus.PENDING) {
            throw new ReportAlreadyProcessedException();
        }

        UUID reportedUserPublicId = deleteContentAndResolveUser(report);

        eventPublisher.publishEvent(new ReportApprovedEvent(
                reportId,
                report.getReporterPublicId(),
                reportedUserPublicId,
                report.getTargetType(),
                report.getTargetId()
        ));

        reportRepository.updateStatusByTargetTypeAndTargetId(report.getTargetType(), report.getTargetId(), ReportStatus.APPROVED);
    }

    private UUID deleteContentAndResolveUser(ReportEntity report) {
        return switch (report.getTargetType()) {
            case POST -> {
                PostEntity post = postRepository.findByIdAndStatus(report.getTargetId(), PostStatus.ACTIVE).orElse(null);
                if (post != null) post.delete();
                yield post != null && post.getAuthor() != null ? post.getAuthor().getPublicId() : null;
            }
            case COMMENT -> {
                CommentEntity comment = commentRepository.findByIdAndStatus(report.getTargetId(), CommentStatus.ACTIVE).orElse(null);
                if (comment != null) comment.delete();
                yield comment != null && comment.getAuthor() != null ? comment.getAuthor().getPublicId() : null;
            }
            case CHAT_ROOM -> {
                chatRoomRepository.findById(report.getTargetId())
                        .filter(r -> !r.isEnded())
                        .ifPresent(room -> {
                            chatMessageOutboxRepository.deleteAllByRoomPublicId(room.getPublicId());
                            chatMessageRepository.deleteAllByRoomId(room.getId());
                            chatRoomMemberRepository.deleteAllByRoomId(room.getId());
                            room.end();
                        });
                yield null;
            }
        };
    }
}
