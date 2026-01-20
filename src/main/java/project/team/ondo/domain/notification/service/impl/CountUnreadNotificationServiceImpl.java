package project.team.ondo.domain.notification.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.notification.repository.NotificationRepository;
import project.team.ondo.domain.notification.service.CountUnreadNotificationService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.global.security.jwt.service.CurrentUserProvider;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CountUnreadNotificationServiceImpl implements CountUnreadNotificationService {

    private final NotificationRepository notificationRepository;
    private final CurrentUserProvider currentUserProvider;

    @Transactional(readOnly = true)
    @Override
    public long execute() {
        UserEntity me = currentUserProvider.getCurrentUser();
        UUID myPublicId = me.getPublicId();
        return notificationRepository.countByReceiverPublicIdAndReadFalse(myPublicId);
    }
}
