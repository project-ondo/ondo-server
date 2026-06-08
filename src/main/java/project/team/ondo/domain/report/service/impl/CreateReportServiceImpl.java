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
import project.team.ondo.domain.report.constant.ReportStatus;
import project.team.ondo.domain.report.constant.ReportTargetType;
import project.team.ondo.domain.report.data.request.CreateReportRequest;
import project.team.ondo.domain.report.entity.ReportEntity;
import project.team.ondo.domain.report.event.ReportCreatedEvent;
import project.team.ondo.domain.report.exception.DuplicatePendingReportException;
import project.team.ondo.domain.report.exception.InvalidReportTargetIdException;
import project.team.ondo.domain.report.exception.ReportTargetNotFoundException;
import project.team.ondo.domain.report.exception.SelfReportException;
import project.team.ondo.domain.report.repository.ReportRepository;
import project.team.ondo.domain.report.service.CreateReportService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.repository.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateReportServiceImpl implements CreateReportService {

    private final ReportRepository reportRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public Long execute(UserEntity reporter, CreateReportRequest request) {
        Long targetInternalId = resolveInternalId(reporter, request.targetType(), request.targetId());
        String snapshot = buildSnapshot(request.targetType(), targetInternalId, request.targetId());

        if (request.targetType() == ReportTargetType.USER) {
            if (reportRepository.existsByReporterPublicIdAndTargetTypeAndTargetIdAndStatus(
                    reporter.getPublicId(), ReportTargetType.USER, targetInternalId, ReportStatus.PENDING)) {
                throw new DuplicatePendingReportException();
            }
        }

        ReportEntity report = ReportEntity.create(
                reporter.getPublicId(),
                request.targetType(),
                targetInternalId,
                request.description()
        );
        reportRepository.save(report);

        eventPublisher.publishEvent(new ReportCreatedEvent(
                report.getId(),
                reporter.getPublicId(),
                request.targetType(),
                targetInternalId,
                request.description(),
                snapshot
        ));

        return report.getId();
    }

    private Long resolveInternalId(UserEntity reporter, ReportTargetType targetType, String rawTargetId) {
        return switch (targetType) {
            case POST -> parseLong(rawTargetId);
            case COMMENT -> parseLong(rawTargetId);
            case CHAT_ROOM -> parseLong(rawTargetId);
            case USER -> {
                UUID targetPublicId = parseUuid(rawTargetId);
                if (reporter.getPublicId().equals(targetPublicId)) {
                    throw new SelfReportException();
                }
                UserEntity targetUser = userRepository.findByPublicId(targetPublicId)
                        .orElseThrow(ReportTargetNotFoundException::new);
                yield targetUser.getId();
            }
        };
    }

    private String buildSnapshot(ReportTargetType targetType, Long internalId, String rawTargetId) {
        return switch (targetType) {
            case POST -> {
                PostEntity post = postRepository.findByIdAndStatus(internalId, PostStatus.ACTIVE)
                        .orElseThrow(ReportTargetNotFoundException::new);
                String content = post.getContent() != null ? post.getContent() : "";
                String preview = content.length() > 200 ? content.substring(0, 200) + "..." : content;
                yield "[게시글] " + post.getTitle() + "\n" + preview;
            }
            case COMMENT -> {
                CommentEntity comment = commentRepository.findByIdAndStatus(internalId, CommentStatus.ACTIVE)
                        .orElseThrow(ReportTargetNotFoundException::new);
                String content = comment.getContent() != null ? comment.getContent() : "";
                String preview = content.length() > 200 ? content.substring(0, 200) + "..." : content;
                yield "[댓글] " + preview;
            }
            case CHAT_ROOM -> {
                ChatRoomEntity room = chatRoomRepository.findById(internalId)
                        .filter(r -> !r.isEnded())
                        .orElseThrow(ReportTargetNotFoundException::new);
                yield "[채팅방] publicId=" + room.getPublicId();
            }
            case USER -> {
                UserEntity user = userRepository.findById(internalId)
                        .orElseThrow(ReportTargetNotFoundException::new);
                yield "[유저] " + user.getDisplayName();
            }
        };
    }

    private Long parseLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new InvalidReportTargetIdException();
        }
    }

    private UUID parseUuid(String value) {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new InvalidReportTargetIdException();
        }
    }
}
