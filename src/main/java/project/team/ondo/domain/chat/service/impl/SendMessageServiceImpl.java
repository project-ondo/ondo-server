package project.team.ondo.domain.chat.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.chat.constant.MessageType;
import project.team.ondo.domain.chat.entity.ChatMessageEntity;
import project.team.ondo.domain.chat.entity.ChatRoomEntity;
import project.team.ondo.domain.chat.entity.ChatRoomMemberEntity;
import project.team.ondo.domain.chat.event.ChatMessageSentEvent;
import project.team.ondo.domain.chat.exception.ChatRoomMemberNotFoundException;
import project.team.ondo.domain.chat.exception.ChatRoomNotFoundException;
import project.team.ondo.domain.chat.exception.UserChatBlockedException;
import project.team.ondo.domain.chat.repository.ChatMessageRepository;
import project.team.ondo.domain.chat.repository.ChatRoomMemberRepository;
import project.team.ondo.domain.chat.repository.ChatRoomRepository;
import project.team.ondo.domain.chat.service.SendMessageService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.repository.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SendMessageServiceImpl implements SendMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public ChatMessageEntity execute(UUID senderPublicId, UUID chatRoomId, MessageType messageType, String content) {
        UserEntity me = userRepository.getByPublicId(senderPublicId);
        ChatRoomEntity chatRoom = chatRoomRepository.findByPublicId(chatRoomId).orElseThrow(ChatRoomNotFoundException::new);

        ChatRoomMemberEntity myMember = chatRoomMemberRepository.findByRoomIdAndUserId(chatRoom.getId(), me.getId())
                .orElseThrow(ChatRoomMemberNotFoundException::new);

        if (!myMember.isActive()) {
            throw new IllegalStateException("USER_LEFT_CHAT_ROOM");
        }
        if (myMember.isBlocked()) {
            throw new UserChatBlockedException();
        }

        Long opponentId = chatRoom.getUserAId().equals(me.getId()) ? chatRoom.getUserBId() : chatRoom.getUserAId();

        ChatRoomMemberEntity opponentMember = chatRoomMemberRepository.findByRoomIdAndUserId(chatRoom.getId(), opponentId)
                .orElseThrow();

        if (opponentMember.isBlocked()) {
            throw new UserChatBlockedException();
        }

        if (!opponentMember.isActive()) {
            opponentMember.join();
        }

        ChatMessageEntity message = chatMessageRepository.save(ChatMessageEntity.create(chatRoom.getId(), me.getId(), messageType, content));

        eventPublisher.publishEvent(
                new ChatMessageSentEvent(message)
        );

        return message;
    }
}
