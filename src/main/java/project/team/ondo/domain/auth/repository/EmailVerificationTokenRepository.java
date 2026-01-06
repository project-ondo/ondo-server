package project.team.ondo.domain.auth.repository;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.auth.entity.EmailVerificationTokenEntity;

@Repository
public interface EmailVerificationTokenRepository extends CrudRepository<@NonNull EmailVerificationTokenEntity, @NonNull String> {
}
