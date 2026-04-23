package project.team.ondo.domain.notification.repository;

import jakarta.persistence.LockModeType;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.notification.constant.NotificationType;
import project.team.ondo.domain.notification.entity.NotificationEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<@NonNull NotificationEntity, @NonNull Long>, NotificationCommandRepository {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<NotificationEntity> findTopByReceiverPublicIdAndTypeAndTargetAndReadFalseOrderByCreatedAtDesc(
            UUID receiverPublicId,
            NotificationType type,
            String target
    );

    Page<@NonNull NotificationEntity> findByReceiverPublicIdOrderByCreatedAtDesc(UUID receiverPublicId, Pageable pageable);

    long countByReceiverPublicIdAndReadFalse(UUID receiverPublicId);

    Optional<NotificationEntity> findByIdAndReceiverPublicId(Long id, UUID receiverPublicId);

    long deleteByReceiverPublicId(UUID receiverPublicId);

    long deleteByReceiverPublicIdAndReadTrue(UUID receiverPublicId);
}
