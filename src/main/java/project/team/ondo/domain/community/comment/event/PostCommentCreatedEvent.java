package project.team.ondo.domain.community.comment.event;

import java.util.UUID;

public record PostCommentCreatedEvent(
        Long postId,
        Long commentId,
        UUID actorPublicId,
        UUID receiverPublicId
) {
}
