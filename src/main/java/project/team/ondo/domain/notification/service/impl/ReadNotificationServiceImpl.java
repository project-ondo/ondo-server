package project.team.ondo.domain.notification.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.notification.entity.NotificationEntity;
import project.team.ondo.domain.notification.exception.NotificationNotFoundException;
import project.team.ondo.domain.notification.repository.NotificationRepository;
import project.team.ondo.domain.notification.service.ReadNotificationService;
import project.team.ondo.domain.user.entity.UserEntity;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadNotificationServiceImpl implements ReadNotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    @Override
    public void execute(UserEntity me, Long notificationId) {
        UUID myPublicId = me.getPublicId();

        NotificationEntity notification = notificationRepository
                .findByIdAndReceiverPublicId(notificationId, myPublicId)
                .orElseThrow(NotificationNotFoundException::new);

        notification.markRead();
    }
}
