package project.team.ondo.domain.chat.event;

import java.util.UUID;

public record ChatRoomReadMarkedEvent(
        Long chatRoomId,
        UUID chatRoomPublicId,
        Long readerId,
        UUID readerPublicId,
        Long lastReadMessageId
) {
}
