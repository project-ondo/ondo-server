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
import project.team.ondo.domain.report.constant.ReportTargetType;
import project.team.ondo.domain.report.data.request.CreateReportRequest;
import project.team.ondo.domain.report.entity.ReportEntity;
import project.team.ondo.domain.report.event.ReportCreatedEvent;
import project.team.ondo.domain.report.exception.ReportTargetNotFoundException;
import project.team.ondo.domain.report.repository.ReportRepository;
import project.team.ondo.domain.report.service.CreateReportService;
import project.team.ondo.domain.user.entity.UserEntity;

@Service
@RequiredArgsConstructor
public class CreateReportServiceImpl implements CreateReportService {

    private final ReportRepository reportRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public Long execute(UserEntity reporter, CreateReportRequest request) {
        String snapshot = resolveSnapshot(request.targetType(), request.targetId());

        ReportEntity report = ReportEntity.create(
                reporter.getPublicId(),
                request.targetType(),
                request.targetId(),
                request.description()
        );
        reportRepository.save(report);

        eventPublisher.publishEvent(new ReportCreatedEvent(
                report.getId(),
                reporter.getPublicId(),
                request.targetType(),
                request.targetId(),
                request.description(),
                snapshot
        ));

        return report.getId();
    }

    private String resolveSnapshot(ReportTargetType targetType, Long targetId) {
        return switch (targetType) {
            case POST -> {
                PostEntity post = postRepository.findByIdAndStatus(targetId, PostStatus.ACTIVE)
                        .orElseThrow(ReportTargetNotFoundException::new);
                String content = post.getContent() != null ? post.getContent() : "";
                String preview = content.length() > 200 ? content.substring(0, 200) + "..." : content;
                yield "[게시글] " + post.getTitle() + "\n" + preview;
            }
            case COMMENT -> {
                CommentEntity comment = commentRepository.findByIdAndStatus(targetId, CommentStatus.ACTIVE)
                        .orElseThrow(ReportTargetNotFoundException::new);
                String content = comment.getContent() != null ? comment.getContent() : "";
                String preview = content.length() > 200 ? content.substring(0, 200) + "..." : content;
                yield "[댓글] " + preview;
            }
            case CHAT_ROOM -> {
                ChatRoomEntity room = chatRoomRepository.findById(targetId)
                        .filter(r -> !r.isEnded())
                        .orElseThrow(ReportTargetNotFoundException::new);
                yield "[채팅방] publicId=" + room.getPublicId();
            }
        };
    }
}
