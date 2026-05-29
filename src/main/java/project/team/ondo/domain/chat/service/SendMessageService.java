package project.team.ondo.domain.chat.service;

import project.team.ondo.domain.chat.constant.MessageType;
import project.team.ondo.domain.chat.entity.ChatMessageEntity;
import project.team.ondo.domain.user.entity.UserEntity;

import java.util.UUID;

public interface SendMessageService {
    ChatMessageEntity execute(UserEntity me, UUID chatRoomId, MessageType messageType, String content);
}
