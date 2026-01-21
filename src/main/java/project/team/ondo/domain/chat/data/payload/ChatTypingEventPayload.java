package project.team.ondo.domain.chat.data.payload;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatTypingEventPayload(
        UUID chatRoomPublicId,
        UUID userPublicId,
        boolean typing,
        LocalDateTime at
) {
}
