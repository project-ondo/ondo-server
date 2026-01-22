package project.team.ondo.domain.chat.data.payload;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatRoomListUpdatePayload(
        UUID chatRoomPublicId,
        long unreadCount,
        String lastMessagePreview,
        LocalDateTime lastMessageAt
) {
}
