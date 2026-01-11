package project.team.ondo.domain.user.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.team.ondo.domain.user.entity.UserEntity;

public interface UserRecommendRepository {
    Page<@NonNull UserEntity> recommend(UserEntity me, Pageable pageable);
}
