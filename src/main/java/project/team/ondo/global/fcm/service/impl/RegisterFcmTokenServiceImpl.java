package project.team.ondo.global.fcm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.global.fcm.entity.UserFcmTokenEntity;
import project.team.ondo.global.fcm.repository.UserFcmTokenRepository;
import project.team.ondo.global.fcm.service.RegisterFcmTokenService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterFcmTokenServiceImpl implements RegisterFcmTokenService {

    private final UserFcmTokenRepository userFcmTokenRepository;

    @Transactional
    @Override
    public void execute(UserEntity me, String token) {
        UUID myPublicId = me.getPublicId();

        userFcmTokenRepository.findByToken(token).ifPresentOrElse(existing -> {
            if (!existing.getUserPublicId().equals(myPublicId)) {
                existing.changeOwner(myPublicId);
            }
            existing.activate();
        }, () -> {
            userFcmTokenRepository.save(UserFcmTokenEntity.create(myPublicId, token));
        });
    }
}
