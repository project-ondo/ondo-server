package project.team.ondo.domain.notification.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.notification.constant.NotificationType;
import project.team.ondo.domain.notification.data.NotificationResult;
import project.team.ondo.domain.notification.entity.NotificationEntity;
import project.team.ondo.domain.notification.repository.NotificationRepository;
import project.team.ondo.domain.notification.service.CreateNotificationService;
import project.team.ondo.domain.notification.service.NotificationAggregationService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateNotificationServiceImpl implements CreateNotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationAggregationService notificationAggregationService;

    @Override
    @Transactional
    public void create(UUID receiverPublicId, NotificationType type, String title, String body, String target) {
        notificationRepository.save(NotificationEntity.create(receiverPublicId, type, title, body, target));
    }

    @Override
    @Transactional
    public NotificationResult createOrAggregate(UUID receiverPublicId, NotificationType type, String target,
                                                String title, String singleBody, String aggregatedBodyPrefix) {
        NotificationEntity saved = notificationAggregationService.saveOrAggregate(
                receiverPublicId, type, target, title, singleBody, aggregatedBodyPrefix
        );
        return new NotificationResult(saved.getId(), saved.getBody(), saved.getGroupCount());
    }
}
