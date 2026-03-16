package project.team.ondo.global.fcm.service;

import project.team.ondo.domain.user.entity.UserEntity;

public interface DeactivateFcmTokenService {
    void execute(UserEntity me, String token);
}
