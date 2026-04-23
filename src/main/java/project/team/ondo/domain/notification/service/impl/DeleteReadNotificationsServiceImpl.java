package project.team.ondo.domain.notification.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.notification.repository.NotificationRepository;
import project.team.ondo.domain.notification.service.DeleteReadNotificationsService;
import project.team.ondo.domain.user.entity.UserEntity;

@Service
@RequiredArgsConstructor
public class DeleteReadNotificationsServiceImpl implements DeleteReadNotificationsService {

    private final NotificationRepository notificationRepository;

    @Transactional
    @Override
    public long execute(UserEntity me) {
        return notificationRepository.deleteByReceiverPublicIdAndReadTrue(me.getPublicId());
    }
}
