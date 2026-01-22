package project.team.ondo.domain.notification.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.notification.data.request.UpdateMyNotificationSettingRequest;
import project.team.ondo.domain.notification.entity.UserNotificationSettingEntity;
import project.team.ondo.domain.notification.repository.UserNotificationSettingRepository;
import project.team.ondo.domain.notification.service.UpdateMyNotificationSettingService;
import project.team.ondo.global.security.jwt.service.CurrentUserProvider;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateMyNotificationSettingServiceImpl implements UpdateMyNotificationSettingService {

    private final UserNotificationSettingRepository userNotificationSettingRepository;
    private final CurrentUserProvider currentUserProvider;

    @Transactional
    public void execute(UpdateMyNotificationSettingRequest request) {
        UUID myPublicId = currentUserProvider.getCurrentUser().getPublicId();
        UserNotificationSettingEntity setting = userNotificationSettingRepository.findByUserPublicId(myPublicId)
                .orElseGet(() -> userNotificationSettingRepository.save(UserNotificationSettingEntity.defaultOf(myPublicId)));

        setting.update(request.pushEnabled(), request.chatEnabled(), request.communityEnabled(),
                request.dndEnabled(), request.dndStart(), request.dndEnd());
    }
}
