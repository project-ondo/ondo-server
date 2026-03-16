package project.team.ondo.global.fcm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.global.fcm.repository.UserFcmTokenRepository;
import project.team.ondo.global.fcm.service.DeactivateFcmTokenService;

@Service
@RequiredArgsConstructor
public class DeactivateFcmTokenServiceImpl implements DeactivateFcmTokenService {

    private final UserFcmTokenRepository userFcmTokenRepository;

    @Transactional
    @Override
    public void execute(UserEntity me, String token) {
        userFcmTokenRepository.deactivateToken(me.getPublicId(), token);
    }
}
