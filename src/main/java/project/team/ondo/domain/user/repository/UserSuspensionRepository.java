package project.team.ondo.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.user.entity.UserSuspensionEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserSuspensionRepository extends JpaRepository<UserSuspensionEntity, Long> {
    Optional<UserSuspensionEntity> findByUserPublicId(UUID userPublicId);
    void deleteByUserPublicId(UUID userPublicId);
}
