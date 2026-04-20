package project.team.ondo.domain.notification.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.notification.data.response.MyNotificationSettingResponse;
import project.team.ondo.domain.notification.entity.UserNotificationSettingEntity;
import project.team.ondo.domain.notification.repository.UserNotificationSettingRepository;
import project.team.ondo.domain.notification.service.GetMyNotificationSettingService;
import project.team.ondo.domain.user.entity.UserEntity;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetMyNotificationSettingServiceImpl implements GetMyNotificationSettingService {

    private final UserNotificationSettingRepository userNotificationSettingRepository;

    @Transactional
    @Override
    public MyNotificationSettingResponse execute(UserEntity me) {
        UUID myPublicId = me.getPublicId();
        UserNotificationSettingEntity setting = userNotificationSettingRepository.getOrCreateFor(myPublicId);
        return MyNotificationSettingResponse.from(setting);
    }
}
