package project.team.ondo.domain.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.chat.repository.ChatRoomMemberCommandRepository;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.repository.UserRepository;
import project.team.ondo.domain.user.service.UserWithdrawService;
import project.team.ondo.global.fcm.service.DeactivateAllFcmTokenService;

@Service
@RequiredArgsConstructor
public class UserWithdrawServiceImpl implements UserWithdrawService {

    private final UserRepository userRepository;
    private final ChatRoomMemberCommandRepository chatRoomMemberCommandRepository;
    private final DeactivateAllFcmTokenService deactivateAllFcmTokenService;

    @Transactional
    @Override
    public void execute(UserEntity me) {
        UserEntity managed = userRepository.getByPublicId(me.getPublicId());
        managed.withdraw();
        chatRoomMemberCommandRepository.deactivateBothSidesByUserId(managed.getId());
        deactivateAllFcmTokenService.execute(managed);
    }
}