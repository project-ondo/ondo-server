package project.team.ondo.domain.chat.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.chat.entity.ChatRoomEntity;
import project.team.ondo.domain.chat.event.ChatMemberLeftEvent;
import project.team.ondo.domain.chat.exception.ChatRoomMemberNotFoundException;
import project.team.ondo.domain.chat.exception.ChatRoomNotFoundException;
import project.team.ondo.domain.chat.repository.ChatMessageOutboxRepository;
import project.team.ondo.domain.chat.repository.ChatMessageRepository;
import project.team.ondo.domain.chat.repository.ChatRoomMemberRepository;
import project.team.ondo.domain.chat.repository.ChatRoomRepository;
import project.team.ondo.domain.chat.service.LeaveRoomService;
import project.team.ondo.domain.user.entity.UserEntity;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LeaveRoomServiceImpl implements LeaveRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageOutboxRepository chatMessageOutboxRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public void execute(UserEntity me, UUID chatRoomId) {
        ChatRoomEntity chatRoom = chatRoomRepository.findByPublicId(chatRoomId)
                .orElseThrow(ChatRoomNotFoundException::new);

        chatRoomMemberRepository.findByRoomIdAndUserId(chatRoom.getId(), me.getId())
                .orElseThrow(ChatRoomMemberNotFoundException::new);

        if (chatRoom.isEnded()) return;

        long opponentUserId = chatRoom.getUserAId().equals(me.getId())
                ? chatRoom.getUserBId()
                : chatRoom.getUserAId();

        boolean hadMessages = chatMessageRepository.existsByRoomId(chatRoom.getId());

        chatMessageOutboxRepository.deleteAllByRoomPublicId(chatRoom.getPublicId());
        chatMessageRepository.deleteAllByRoomId(chatRoom.getId());
        chatRoomMemberRepository.deleteAllByRoomId(chatRoom.getId());
        chatRoom.end();

        eventPublisher.publishEvent(new ChatMemberLeftEvent(
                chatRoom.getPublicId(),
                me.getId(),
                opponentUserId,
                hadMessages
        ));
    }
}
