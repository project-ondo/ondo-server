package project.team.ondo.domain.chat.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.chat.entity.ChatRoomEntity;
import project.team.ondo.domain.chat.entity.ChatRoomMemberEntity;
import project.team.ondo.domain.chat.repository.ChatRoomMemberRepository;
import project.team.ondo.domain.chat.repository.ChatRoomRepository;
import project.team.ondo.domain.chat.service.UnblockRoomService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.global.security.jwt.service.CurrentUserProvider;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UnblockRoomServiceImpl implements UnblockRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final CurrentUserProvider currentUserProvider;

    @Override
    @Transactional
    public void execute(UUID chatRoomId) {
        UserEntity me = currentUserProvider.getCurrentUser();
        ChatRoomEntity room = chatRoomRepository.findByPublicId(chatRoomId).orElseThrow();

        ChatRoomMemberEntity member = chatRoomMemberRepository.findByRoomIdAndUserId(room.getId(), me.getId())
                .orElseThrow();

        member.unblock();
    }
}
