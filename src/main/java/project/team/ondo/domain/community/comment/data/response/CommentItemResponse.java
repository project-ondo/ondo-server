package project.team.ondo.domain.community.comment.data.response;

import project.team.ondo.domain.community.comment.entity.CommentEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentItemResponse(
        Long commentId,
        UUID userPublicId,
        String content,
        String authorName,
        LocalDateTime createdAt
) {
    public static CommentItemResponse from(CommentEntity comment) {
        return new CommentItemResponse(
                comment.getId(),
                comment.getAuthor().getPublicId(),
                comment.getContent(),
                comment.getAuthor().getDisplayName(),
                comment.getCreatedAt()
        );
    }
}
