package project.team.ondo.domain.chat.data.payload;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatPresencePayload(
        UUID userPublicId,
        boolean online,
        UUID viewingChatRoomPublicId,
        LocalDateTime at
) {
}
