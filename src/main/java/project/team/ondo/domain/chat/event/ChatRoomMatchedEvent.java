package project.team.ondo.domain.chat.event;

import java.util.UUID;

public record ChatRoomMatchedEvent(
        UUID receiverPublicId,
        UUID chatRoomPublicId,
        UUID senderPublicId
) {
}
