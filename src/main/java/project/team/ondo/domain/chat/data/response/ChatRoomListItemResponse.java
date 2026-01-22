package project.team.ondo.domain.chat.data.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatRoomListItemResponse(
        UUID roomId,
        UUID opponentPublicId,
        String opponentDisplayName,
        String opponentProfileImageUrl,
        boolean opponentOnline,
        long unreadCount,
        String lastMessagePreview,
        boolean muted,
        LocalDateTime lastMessageAt
) {
}
