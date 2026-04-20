package project.team.ondo.domain.chat.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.chat.data.ChatRoomInfo;
import project.team.ondo.domain.chat.entity.ChatRoomEntity;
import project.team.ondo.domain.chat.entity.ChatRoomMemberEntity;
import project.team.ondo.domain.chat.exception.ChatRoomMemberNotFoundException;
import project.team.ondo.domain.chat.exception.ChatRoomNotFoundException;
import project.team.ondo.domain.chat.repository.ChatRoomMemberRepository;
import project.team.ondo.domain.chat.repository.ChatRoomRepository;
import project.team.ondo.domain.chat.service.ChatRoomMembershipService;
import project.team.ondo.global.contract.RoomMembershipQueryPort;
import project.team.ondo.global.contract.RoomMembershipResult;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomMembershipServiceImpl implements ChatRoomMembershipService, RoomMembershipQueryPort {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    @Override
    @Transactional(readOnly = true)
    public ChatRoomInfo validateAndGet(UUID roomPublicId, Long userId) {
        ChatRoomEntity room = chatRoomRepository.findByPublicId(roomPublicId)
                .orElseThrow(ChatRoomNotFoundException::new);

        chatRoomMemberRepository.findByRoomIdAndUserId(room.getId(), userId)
                .orElseThrow(ChatRoomMemberNotFoundException::new);

        List<ChatRoomMemberEntity> members = chatRoomMemberRepository.findAllByRoomId(room.getId());
        boolean matchEnded = members.size() >= 2 && members.stream().noneMatch(ChatRoomMemberEntity::isActive);

        return new ChatRoomInfo(room.getId(), room.getUserAId(), room.getUserBId(), matchEnded);
    }

    @Override
    @Transactional(readOnly = true)
    public RoomMembershipResult query(UUID chatRoomPublicId, Long userId) {
        ChatRoomInfo info = validateAndGet(chatRoomPublicId, userId);
        Long opponentId = info.userAId().equals(userId) ? info.userBId() : info.userAId();
        return new RoomMembershipResult(info.roomId(), opponentId, info.matchEnded());
    }
}
