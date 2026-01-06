package project.team.ondo.domain.user.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.user.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<@NonNull UserEntity,@NonNull Long> {
    boolean existsByEmail(String email);
}
