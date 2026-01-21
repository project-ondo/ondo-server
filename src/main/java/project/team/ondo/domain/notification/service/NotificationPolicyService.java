package project.team.ondo.domain.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.notification.constant.NotificationType;
import project.team.ondo.domain.notification.entity.UserNotificationSettingEntity;
import project.team.ondo.domain.notification.repository.UserNotificationSettingRepository;

import java.time.LocalTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationPolicyService {

    private final UserNotificationSettingRepository userNotificationSettingRepository;

    @Transactional
    public boolean shouldSendPush(UUID receiverPublicId, NotificationType notificationType) {
        UserNotificationSettingEntity setting = userNotificationSettingRepository.findByUserPublicId(receiverPublicId)
                .orElseGet(() -> userNotificationSettingRepository.save(UserNotificationSettingEntity.defaultOf(receiverPublicId)));

        if (!setting.isPushEnabled()) return false;

        if (notificationType == NotificationType.CHAT_MESSAGE || notificationType == NotificationType.MATCH_CREATED) {
            if (!setting.isChatEnabled()) return false;
        } else if (notificationType == NotificationType.POST_COMMENT || notificationType == NotificationType.POST_LIKE) {
            if (!setting.isCommunityEnabled()) return false;
        }

        if (!setting.isDndEnabled()) return true;
        if (setting.getDndStart() == null || setting.getDndEnd() == null) return true;

        LocalTime now = LocalTime.now();
        LocalTime dndStart = setting.getDndStart();
        LocalTime dndEnd = setting.getDndEnd();

        boolean inDndPeriod;

        if (dndStart.equals(dndEnd)) {
            inDndPeriod = false;
        } else if (dndStart.isBefore(dndEnd)) {
            inDndPeriod = !now.isBefore(dndStart) && now.isBefore(dndEnd);
        } else {
            inDndPeriod = !now.isBefore(dndStart) || now.isBefore(dndEnd);
        }

        return !inDndPeriod;
    }
}
