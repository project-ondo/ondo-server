package project.team.ondo.domain.chat.data.payload;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatReadEventPayload(
        UUID chatRoomPublicId,
        UUID readerPublicId,
        Long lastReadMessageId,
        LocalDateTime at
) {
}
