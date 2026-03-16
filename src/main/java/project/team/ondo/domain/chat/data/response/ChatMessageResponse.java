package project.team.ondo.domain.chat.data.response;

import project.team.ondo.domain.chat.constant.MessageType;
import project.team.ondo.domain.chat.entity.ChatMessageEntity;

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
    public static ChatMessageResponse from(ChatMessageEntity message, UUID roomPublicId) {
        return new ChatMessageResponse(
                message.getId(),
                roomPublicId,
                message.getSenderId(),
                message.getMessageType(),
                message.getContent(),
                message.getCreatedAt()
        );
    }
}
