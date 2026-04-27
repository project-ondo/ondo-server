package project.team.ondo.domain.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.team.ondo.domain.notification.constant.NotificationType;
import project.team.ondo.domain.notification.entity.NotificationEntity;
import project.team.ondo.global.lock.DistributedLockService;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationAggregationService {

    private static final Duration LOCK_TTL = Duration.ofSeconds(5);

    private final NotificationAggregationPersistenceService persistenceService;
    private final DistributedLockService lockService;

    public NotificationEntity saveOrAggregate(
            UUID receiverPublicId,
            NotificationType type,
            String target,
            String title,
            String singleBody,
            String aggregatedBodyPrefix
    ) {
        String lockKey = buildLockKey(receiverPublicId, type, target);
        return lockService.executeWithLock(lockKey, LOCK_TTL,
                () -> persistenceService.saveOrAggregate(
                        receiverPublicId, type, target, title, singleBody, aggregatedBodyPrefix));
    }

    private String buildLockKey(UUID receiverPublicId, NotificationType type, String target) {
        return "notification:agg:" + receiverPublicId + ":" + type + ":" + (target != null ? target : "");
    }
}