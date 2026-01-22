package project.team.ondo.domain.chat.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatMatchEndedEvent(
        Long chatRoomId,
        UUID chatRoomPublicId,
        Long userAId,
        Long userBId,
        LocalDateTime endedAt
) {
}
