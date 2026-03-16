package project.team.ondo.domain.notification.service;

import project.team.ondo.domain.user.entity.UserEntity;

public interface ReadAllNotificationService {
    long execute(UserEntity me);
}
