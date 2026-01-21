package project.team.ondo.domain.notification.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.notification.data.response.MyNotificationSettingResponse;
import project.team.ondo.domain.notification.entity.UserNotificationSettingEntity;
import project.team.ondo.domain.notification.repository.UserNotificationSettingRepository;
import project.team.ondo.domain.notification.service.GetMyNotificationSettingService;
import project.team.ondo.global.security.jwt.service.CurrentUserProvider;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetMyNotificationSettingServiceImpl implements GetMyNotificationSettingService {

    private final UserNotificationSettingRepository userNotificationSettingRepository;
    private final CurrentUserProvider currentUserProvider;

    @Transactional
    @Override
    public MyNotificationSettingResponse execute() {
        UUID myPublicId = currentUserProvider.getCurrentUser().getPublicId();
        UserNotificationSettingEntity setting = userNotificationSettingRepository.findByUserPublicId(myPublicId)
                .orElseGet(() -> userNotificationSettingRepository.save(UserNotificationSettingEntity.defaultOf(myPublicId)));
        return MyNotificationSettingResponse.from(setting);
    }
}
