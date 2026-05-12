package project.team.ondo.global.fcm.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.team.ondo.global.fcm.entity.UserFcmTokenEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserFcmTokenRepository extends JpaRepository<@NonNull UserFcmTokenEntity, @NonNull Long>, UserFcmTokenQueryRepository {
    Optional<UserFcmTokenEntity> findByToken(String token);

    @Modifying
    @Query("DELETE FROM UserFcmTokenEntity t WHERE t.userPublicId = :userPublicId")
    void deleteAllByUserPublicId(@Param("userPublicId") UUID userPublicId);
}
