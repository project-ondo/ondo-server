package project.team.ondo.domain.chat.data.response;

import project.team.ondo.domain.chat.constant.MessageType;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatMessageResponse(
        Long messageId,
        UUID roomId,
        Long senderId,
        MessageType messageType,
        String content,
        LocalDateTime createdAt
) {
}
