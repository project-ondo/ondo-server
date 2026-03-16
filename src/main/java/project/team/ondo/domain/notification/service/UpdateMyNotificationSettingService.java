package project.team.ondo.domain.notification.service;

import project.team.ondo.domain.notification.data.request.UpdateMyNotificationSettingRequest;
import project.team.ondo.domain.user.entity.UserEntity;

public interface UpdateMyNotificationSettingService {
    void execute(UserEntity me, UpdateMyNotificationSettingRequest request);
}
