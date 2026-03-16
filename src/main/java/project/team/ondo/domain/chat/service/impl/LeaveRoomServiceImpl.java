package project.team.ondo.domain.chat.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.chat.entity.ChatRoomEntity;
import project.team.ondo.domain.chat.entity.ChatRoomMemberEntity;
import project.team.ondo.domain.chat.event.ChatMatchEndedEvent;
import project.team.ondo.domain.chat.exception.ChatRoomMemberNotFoundException;
import project.team.ondo.domain.chat.exception.ChatRoomNotFoundException;
import project.team.ondo.domain.chat.repository.ChatRoomMemberRepository;
import project.team.ondo.domain.chat.repository.ChatRoomRepository;
import project.team.ondo.domain.chat.service.LeaveRoomService;
import project.team.ondo.domain.user.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LeaveRoomServiceImpl implements LeaveRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public void execute(UserEntity me, UUID chatRoomId) {
        ChatRoomEntity chatRoom = chatRoomRepository.findByPublicId(chatRoomId)
                .orElseThrow(ChatRoomNotFoundException::new);

        ChatRoomMemberEntity chatRoomMember = chatRoomMemberRepository.findByRoomIdAndUserId(chatRoom.getId(), me.getId())
                .orElseThrow(ChatRoomMemberNotFoundException::new);

        if (!chatRoomMember.isActive()) return;

        chatRoomMember.leave();

        List<ChatRoomMemberEntity> members = chatRoomMemberRepository.findAllByRoomId(chatRoom.getId());

        boolean allLeft = members.stream()
                .allMatch(member -> !member.isActive());

        if (!allLeft) return;

        Long userAId = chatRoom.getUserAId();
        Long userBId = chatRoom.getUserBId();

        eventPublisher.publishEvent(
                new ChatMatchEndedEvent(
                        chatRoom.getId(),
                        chatRoom.getPublicId(),
                        userAId,
                        userBId,
                        LocalDateTime.now()
                )
        );
    }
}
