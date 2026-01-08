package project.team.ondo.domain.user.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.user.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<@NonNull UserEntity,@NonNull Long> {
    boolean existsByEmail(String email);

    boolean existsByLoginId(String loginId);

    boolean existsByDisplayName(String displayName);

    Optional<UserEntity> findByLoginId(String loginId);

    Optional<UserEntity> findByPublicId(UUID publicId);
}
