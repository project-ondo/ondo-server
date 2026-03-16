package project.team.ondo.domain.user.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.team.ondo.domain.user.data.request.UserSearchCondition;
import project.team.ondo.domain.user.entity.UserEntity;

public interface UserSearchQueryRepository {
    Page<@NonNull UserEntity> searchUser(UserSearchCondition condition, Pageable pageable);
}
