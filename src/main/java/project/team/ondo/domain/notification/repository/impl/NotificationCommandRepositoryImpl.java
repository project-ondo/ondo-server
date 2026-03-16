package project.team.ondo.domain.notification.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.team.ondo.domain.notification.entity.QNotificationEntity;
import project.team.ondo.domain.notification.repository.NotificationCommandRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationCommandRepositoryImpl implements NotificationCommandRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QNotificationEntity notification = QNotificationEntity.notificationEntity;

    @Override
    public long markAllRead(UUID receiverPublicId) {
        return jpaQueryFactory
                .update(notification)
                .set(notification.read, true)
                .where(
                        notification.receiverPublicId.eq(receiverPublicId),
                        notification.read.eq(false)
                )
                .execute();
    }
}
