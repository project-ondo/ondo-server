package project.team.ondo.global.security.jwt.repository;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.team.ondo.global.security.jwt.entity.RefreshTokenEntity;

@Repository
public interface RefreshTokenRepository extends CrudRepository<@NonNull RefreshTokenEntity,@NonNull String> {
}
