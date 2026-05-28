package project.team.ondo.domain.auth.repository;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.auth.entity.PasswordResetTokenEntity;

@Repository
public interface PasswordResetTokenRepository extends CrudRepository<@NonNull PasswordResetTokenEntity, @NonNull String> {
}