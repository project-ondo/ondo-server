package project.team.ondo.domain.auth.repository;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.auth.entity.AuthenticationEntity;

import java.util.Optional;

@Repository
public interface AuthenticationRepository extends CrudRepository<@NonNull AuthenticationEntity, @NonNull String> {

    Optional<AuthenticationEntity> findByEmail(String email);

    Boolean existsAuthenticationByEmail(String email);

    AuthenticationEntity saveAuthentication(AuthenticationEntity authenticationEntity);
}
