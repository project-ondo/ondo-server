package project.team.ondo.domain.notification.service;

import project.team.ondo.domain.notification.data.response.MyNotificationSettingResponse;
import project.team.ondo.domain.user.entity.UserEntity;

public interface GetMyNotificationSettingService {
    MyNotificationSettingResponse execute(UserEntity me);
}
