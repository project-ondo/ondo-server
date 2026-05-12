package project.team.ondo.domain.rating.repository;

import project.team.ondo.domain.rating.entity.UserRatingEntity;

import java.util.List;

public interface UserRatingQueryRepository {
    List<UserRatingEntity> findByRateeId(Long rateeId, Long cursor, int size);
}
