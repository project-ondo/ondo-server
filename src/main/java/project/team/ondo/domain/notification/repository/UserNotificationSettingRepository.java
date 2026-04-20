package project.team.ondo.domain.notification.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.notification.entity.UserNotificationSettingEntity;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserNotificationSettingRepository extends JpaRepository<@NonNull UserNotificationSettingEntity, @NonNull Long> {
    Optional<UserNotificationSettingEntity> findByUserPublicId(UUID userPublicId);

    default UserNotificationSettingEntity getOrCreateFor(UUID userPublicId) {
        return findByUserPublicId(userPublicId)
                .orElseGet(() -> save(UserNotificationSettingEntity.defaultOf(userPublicId)));
    }
}
