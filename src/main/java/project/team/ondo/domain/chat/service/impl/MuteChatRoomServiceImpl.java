package project.team.ondo.domain.chat.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.chat.repository.ChatRoomMemberRepository;
import project.team.ondo.domain.chat.service.MuteChatRoomService;
import project.team.ondo.domain.user.entity.UserEntity;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MuteChatRoomServiceImpl implements MuteChatRoomService {

    private final ChatRoomMemberRepository chatRoomMemberRepository;

    @Transactional
    @Override
    public void execute(UserEntity me, UUID chatRoomPublicId) {
        chatRoomMemberRepository.mute(chatRoomPublicId, me.getId());
    }
}
