package project.team.ondo.domain.rating.repository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.rating.entity.QUserRatingEntity;
import project.team.ondo.domain.rating.entity.UserRatingEntity;
import project.team.ondo.domain.rating.repository.UserRatingQueryRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRatingQueryRepositoryImpl implements UserRatingQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QUserRatingEntity userRating = QUserRatingEntity.userRatingEntity;

    @Override
    public List<UserRatingEntity> findByRateeId(Long rateeId, Long cursor, int size) {
        BooleanExpression condition = userRating.rateeId.eq(rateeId);
        if (cursor != null) {
            condition = condition.and(userRating.id.lt(cursor));
        }

        return jpaQueryFactory
                .selectFrom(userRating)
                .where(condition)
                .orderBy(userRating.id.desc())
                .limit(size + 1L)
                .fetch();
    }
}
