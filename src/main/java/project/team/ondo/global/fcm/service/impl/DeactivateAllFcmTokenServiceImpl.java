package project.team.ondo.global.fcm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.global.fcm.repository.UserFcmTokenRepository;
import project.team.ondo.global.fcm.service.DeactivateAllFcmTokenService;

@Service
@RequiredArgsConstructor
public class DeactivateAllFcmTokenServiceImpl implements DeactivateAllFcmTokenService {

    private final UserFcmTokenRepository userFcmTokenRepository;

    @Transactional
    @Override
    public void execute(UserEntity me) {
        userFcmTokenRepository.deactivateAll(me.getPublicId());
    }
}
