package project.team.ondo.global.fcm.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.team.ondo.global.fcm.entity.UserFcmTokenEntity;

import java.util.Optional;

@Repository
public interface UserFcmTokenRepository extends JpaRepository<@NonNull UserFcmTokenEntity, @NonNull Long>, UserFcmTokenQueryRepository {
    Optional<UserFcmTokenEntity> findByToken(String token);
}
