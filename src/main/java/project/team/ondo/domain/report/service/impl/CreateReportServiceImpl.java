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
import project.team.ondo.domain.report.exception.InvalidReportTargetException;
import project.team.ondo.domain.report.exception.ReportTargetNotFoundException;
import project.team.ondo.domain.report.repository.ReportRepository;
import project.team.ondo.domain.report.service.CreateReportService;
import project.team.ondo.domain.user.entity.UserEntity;

import java.util.UUID;

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
        ResolvedTarget target = resolveTarget(reporter, request.targetType(), request.targetId());

        ReportEntity report = ReportEntity.create(
                reporter.getPublicId(),
                request.targetType(),
                target.internalId(),
                request.description()
        );
        reportRepository.save(report);

        eventPublisher.publishEvent(new ReportCreatedEvent(
                report.getId(),
                reporter.getPublicId(),
                request.targetType(),
                target.internalId(),
                request.description(),
                target.snapshot()
        ));

        return report.getId();
    }

    /**
     * 클라이언트가 보낸 식별자 문자열을 type별로 해석해 내부 PK(Long)와 스냅샷을 함께 산출한다.
     * POST/COMMENT는 노출되는 식별자가 곧 내부 PK이고, CHAT_ROOM은 publicId(UUID)이므로 내부 id로 변환한다.
     */
    private ResolvedTarget resolveTarget(UserEntity reporter, ReportTargetType targetType, String rawTargetId) {
        return switch (targetType) {
            case POST -> {
                PostEntity post = postRepository.findByIdAndStatus(parseLong(rawTargetId), PostStatus.ACTIVE)
                        .orElseThrow(ReportTargetNotFoundException::new);
                String content = post.getContent() != null ? post.getContent() : "";
                String preview = content.length() > 200 ? content.substring(0, 200) + "..." : content;
                yield new ResolvedTarget(post.getId(), "[게시글] " + post.getTitle() + "\n" + preview);
            }
            case COMMENT -> {
                CommentEntity comment = commentRepository.findByIdAndStatus(parseLong(rawTargetId), CommentStatus.ACTIVE)
                        .orElseThrow(ReportTargetNotFoundException::new);
                String content = comment.getContent() != null ? comment.getContent() : "";
                String preview = content.length() > 200 ? content.substring(0, 200) + "..." : content;
                yield new ResolvedTarget(comment.getId(), "[댓글] " + preview);
            }
            case CHAT_ROOM -> {
                ChatRoomEntity room = chatRoomRepository.findByPublicId(parseUuid(rawTargetId))
                        .filter(r -> !r.isEnded())
                        .orElseThrow(ReportTargetNotFoundException::new);
                // 참가자만 신고 가능 — 비참가자에게는 존재 자체를 노출하지 않는다.
                if (!isParticipant(room, reporter.getId())) {
                    throw new ReportTargetNotFoundException();
                }
                yield new ResolvedTarget(room.getId(), "[채팅방] publicId=" + room.getPublicId());
            }
        };
    }

    private boolean isParticipant(ChatRoomEntity room, Long userId) {
        return userId.equals(room.getUserAId()) || userId.equals(room.getUserBId());
    }

    private Long parseLong(String raw) {
        try {
            return Long.parseLong(raw);
        } catch (NumberFormatException e) {
            throw new InvalidReportTargetException();
        }
    }

    private UUID parseUuid(String raw) {
        try {
            return UUID.fromString(raw);
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new InvalidReportTargetException();
        }
    }

    private record ResolvedTarget(Long internalId, String snapshot) {}
}
