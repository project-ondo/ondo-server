package project.team.ondo.domain.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.notification.constant.NotificationType;
import project.team.ondo.domain.notification.entity.NotificationEntity;
import project.team.ondo.domain.notification.repository.NotificationRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class NotificationAggregationPersistenceService {

    private static final Duration WINDOW = Duration.ofMinutes(5);

    private final NotificationRepository notificationRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public NotificationEntity saveOrAggregate(
            UUID receiverPublicId,
            NotificationType type,
            String target,
            String title,
            String singleBody,
            String aggregatedBodyPrefix
    ) {
        var existingOpt = notificationRepository
                .findTopByReceiverPublicIdAndTypeAndTargetAndReadFalseOrderByCreatedAtDesc(
                        receiverPublicId, type, target);

        if (existingOpt.isPresent()) {
            NotificationEntity existing = existingOpt.get();
            LocalDateTime createdAt = existing.getCreatedAt();

            if (createdAt != null && Duration.between(createdAt, LocalDateTime.now()).compareTo(WINDOW) <= 0) {
                existing.increaseGroupCount();
                long n = existing.getGroupCount();
                String body = (n <= 1)
                        ? singleBody
                        : aggregatedBodyPrefix.replace("{n}", String.valueOf(n - 1));
                existing.updateTitle(title);
                existing.updateBody(body);
                return existing;
            }
        }

        return notificationRepository.save(
                NotificationEntity.builder()
                        .receiverPublicId(receiverPublicId)
                        .type(type)
                        .title(title)
                        .body(singleBody)
                        .target(target)
                        .groupCount(1L)
                        .read(false)
                        .build()
        );
    }
}