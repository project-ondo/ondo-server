package project.team.ondo.domain.chat.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.chat.repository.ChatRoomMuteCommandRepository;
import project.team.ondo.domain.chat.service.UnmuteChatRoomService;
import project.team.ondo.domain.user.entity.UserEntity;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UnmuteChatRoomServiceImpl implements UnmuteChatRoomService {

    private final ChatRoomMuteCommandRepository chatRoomMuteRepository;

    @Transactional
    @Override
    public void execute(UserEntity me, UUID chatRoomPublicId) {
        chatRoomMuteRepository.unmute(chatRoomPublicId, me.getId());
    }
}
