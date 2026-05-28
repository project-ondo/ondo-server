package project.team.ondo.domain.user.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.user.constant.UserStatus;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<@NonNull UserEntity,@NonNull Long>, UserSearchQueryRepository, UserRecommendQueryRepository {
    boolean existsByEmail(String email);

    boolean existsByLoginId(String loginId);

    boolean existsByDisplayName(String displayName);

    Optional<UserEntity> findByLoginId(String loginId);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByPublicId(UUID publicId);

    boolean existsByPublicId(UUID publicId);

    default UserEntity getByPublicId(UUID publicId) {
        return findByPublicId(publicId).orElseThrow(UserNotFoundException::new);
    }

    @Query("SELECT u FROM UserEntity u WHERE u.status = :status AND u.deletedAt < :threshold")
    List<UserEntity> findAllByStatusAndDeletedAtBefore(
            @Param("status") UserStatus status,
            @Param("threshold") LocalDateTime threshold
    );
}
