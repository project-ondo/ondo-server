package project.team.ondo.domain.chat.event;

import java.util.UUID;

public record ChatMemberLeftEvent(
        UUID roomPublicId,
        Long leftUserId,
        Long opponentUserId,
        boolean hadMessages
) {
}
