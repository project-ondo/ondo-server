package project.team.ondo.domain.chat.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.chat.entity.ChatRoomEntity;
import project.team.ondo.domain.chat.entity.ChatRoomMemberEntity;
import project.team.ondo.domain.chat.event.ChatRoomReadMarkedEvent;
import project.team.ondo.domain.chat.exception.ChatRoomMemberNotFoundException;
import project.team.ondo.domain.chat.exception.ChatRoomNotFoundException;
import project.team.ondo.domain.chat.repository.ChatRoomMemberRepository;
import project.team.ondo.domain.chat.repository.ChatRoomRepository;
import project.team.ondo.domain.chat.service.MarkRoomReadService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.global.security.jwt.service.CurrentUserProvider;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MarkRoomReadServiceImpl implements MarkRoomReadService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final CurrentUserProvider currentUserProvider;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public void execute(UUID chatRoomId, Long lastReadMessageId) {
        UserEntity me = currentUserProvider.getCurrentUser();
        ChatRoomEntity chatRoom = chatRoomRepository.findByPublicId(chatRoomId)
                .orElseThrow(ChatRoomNotFoundException::new);

        ChatRoomMemberEntity chatRoomMember = chatRoomMemberRepository.findByRoomIdAndUserId(chatRoom.getId(), me.getId())
                .orElseThrow(ChatRoomMemberNotFoundException::new);

        long current = chatRoomMember.getLastReadMessageId() == null ? 0L : chatRoomMember.getLastReadMessageId();
        long next = Math.max(current, lastReadMessageId == null ? 0L : lastReadMessageId);

        chatRoomMember.updateLastReadMessageId(next);

        eventPublisher.publishEvent(
                new ChatRoomReadMarkedEvent(
                        chatRoom.getId(),
                        chatRoom.getPublicId(),
                        me.getId(),
                        me.getPublicId(),
                        next
                )
        );
    }
}
