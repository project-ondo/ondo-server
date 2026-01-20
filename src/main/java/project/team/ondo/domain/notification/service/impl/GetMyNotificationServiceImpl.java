package project.team.ondo.domain.notification.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.notification.data.response.NotificationItemResponse;
import project.team.ondo.domain.notification.repository.NotificationRepository;
import project.team.ondo.domain.notification.service.GetMyNotificationService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.global.security.jwt.service.CurrentUserProvider;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetMyNotificationServiceImpl implements GetMyNotificationService {

    private final NotificationRepository notificationRepository;
    private final CurrentUserProvider currentUserProvider;

    @Transactional(readOnly = true)
    @Override
    public Page<@NonNull NotificationItemResponse> execute(Pageable pageable) {
        UserEntity me = currentUserProvider.getCurrentUser();
        UUID myPublicId = me.getPublicId();

        return notificationRepository
                .findByReceiverPublicIdOrderByCreatedAtDesc(myPublicId, pageable)
                .map(NotificationItemResponse::from);
    }
}
