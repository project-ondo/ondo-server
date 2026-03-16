package project.team.ondo.domain.notification.service;

import project.team.ondo.domain.user.entity.UserEntity;

public interface CountUnreadNotificationService {
    long execute(UserEntity me);
}
