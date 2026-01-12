package project.team.ondo.domain.chat.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.chat.constant.MessageType;
import project.team.ondo.domain.chat.entity.ChatMessageEntity;
import project.team.ondo.domain.chat.entity.ChatRoomEntity;
import project.team.ondo.domain.chat.entity.ChatRoomMemberEntity;
import project.team.ondo.domain.chat.exception.ChatRoomMemberNotFoundException;
import project.team.ondo.domain.chat.exception.ChatRoomNotFoundException;
import project.team.ondo.domain.chat.exception.UserChatBlockedException;
import project.team.ondo.domain.chat.repository.ChatMessageRepository;
import project.team.ondo.domain.chat.repository.ChatRoomMemberRepository;
import project.team.ondo.domain.chat.repository.ChatRoomRepository;
import project.team.ondo.domain.chat.service.SendMessageService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.global.security.jwt.service.CurrentUserProvider;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SendMessageServiceImpl implements SendMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final CurrentUserProvider currentUserProvider;

    @Transactional
    @Override
    public ChatMessageEntity execute(UUID chatRoomId, MessageType messageType, String content) {
        UserEntity me = currentUserProvider.getCurrentUser();
        ChatRoomEntity chatRoom = chatRoomRepository.findByPublicId(chatRoomId).orElseThrow(ChatRoomNotFoundException::new);

        ChatRoomMemberEntity chatRoomMember = chatRoomMemberRepository.findByRoomIdAndUserId(chatRoom.getId(), me.getId())
                .orElseThrow(ChatRoomMemberNotFoundException::new);

        if (!chatRoomMember.isActive() || chatRoomMember.isBlocked()) {
            throw new UserChatBlockedException();
        }

        return chatMessageRepository.save(ChatMessageEntity.create(chatRoom.getId(), me.getId(), messageType, content));
    }
}
