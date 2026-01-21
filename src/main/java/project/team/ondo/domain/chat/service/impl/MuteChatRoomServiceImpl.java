package project.team.ondo.domain.chat.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.chat.repository.ChatRoomMuteRepository;
import project.team.ondo.domain.chat.service.MuteChatRoomService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.global.security.jwt.service.CurrentUserProvider;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MuteChatRoomServiceImpl implements MuteChatRoomService {

    private final ChatRoomMuteRepository chatRoomMuteRepository;
    private final CurrentUserProvider currentUserProvider;

    @Transactional
    @Override
    public void execute(UUID chatRoomPublicId) {
        UserEntity me = currentUserProvider.getCurrentUser();
        chatRoomMuteRepository.mute(chatRoomPublicId, me.getId());
    }
}
