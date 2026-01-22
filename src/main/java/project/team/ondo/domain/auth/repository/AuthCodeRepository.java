package project.team.ondo.domain.auth.repository;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.auth.entity.AuthCodeEntity;

@Repository
public interface AuthCodeRepository extends CrudRepository<@NonNull AuthCodeEntity,@NonNull String> {
}
