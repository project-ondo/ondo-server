package project.team.ondo.domain.community.postlike.event;

import java.util.UUID;

public record PostLikedEvent(
        Long postId,
        UUID actorPublicId,
        UUID receiverPublicId
) {
}
