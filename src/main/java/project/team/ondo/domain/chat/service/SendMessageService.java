package project.team.ondo.domain.chat.service;

import project.team.ondo.domain.chat.constant.MessageType;
import project.team.ondo.domain.chat.entity.ChatMessageEntity;

import java.util.UUID;

public interface SendMessageService {
    ChatMessageEntity execute(UUID senderPublicId, UUID chatRoomId, MessageType messageType, String content);
}
