package project.team.ondo.domain.chat.data.response;

import project.team.ondo.domain.chat.constant.MessageType;
import project.team.ondo.domain.chat.entity.ChatMessageEntity;
import project.team.ondo.domain.user.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatMessageResponse(
        Long messageId,
        UUID roomId,
        UUID senderPublicId,
        String senderDisplayName,
        String senderProfileImageKey,
        MessageType messageType,
        String content,
        LocalDateTime createdAt
) {
    public static ChatMessageResponse from(ChatMessageEntity message, UUID roomPublicId, UserEntity sender) {
        return new ChatMessageResponse(
                message.getId(),
                roomPublicId,
                sender.getPublicId(),
                sender.getDisplayName(),
                sender.getProfileImageKey(),
                message.getMessageType(),
                message.getContent(),
                message.getCreatedAt()
        );
    }
}
