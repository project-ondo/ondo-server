package project.team.ondo.domain.user.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.user.constant.UserStatus;

import static project.team.ondo.domain.user.constant.RecommendationWeights.INTEREST_WEIGHT;
import static project.team.ondo.domain.user.constant.RecommendationWeights.MAJOR_WEIGHT;
import project.team.ondo.domain.user.entity.QUserEntity;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.repository.UserRecommendQueryRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRecommendQueryRepositoryImpl implements UserRecommendQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QUserEntity user = QUserEntity.userEntity;

    @Override
    public Page<@NonNull UserEntity> recommend(UserEntity me, Pageable pageable) {
        BooleanBuilder where = new BooleanBuilder();
        where.and(user.status.eq(UserStatus.ACTIVE));
        where.and(user.id.ne(me.getId()));

        List<String> tags = me.getInterests() == null ? List.of() : me.getInterests().stream()
                .filter(str -> str != null && !str.isBlank())
                .map(String::trim)
                .toList();

        StringPath interest = Expressions.stringPath("interest");
        NumberExpression<Long> overlapCount =
                tags.isEmpty() ? Expressions.numberTemplate(Long.class, "0")
                        : interest.count();

        NumberExpression<Long> majorMatchScore = new CaseBuilder()
                .when(user.major.eq(me.getMajor()))
                .then(MAJOR_WEIGHT)
                .otherwise(0L);

        NumberExpression<Long> score = overlapCount.multiply(INTEREST_WEIGHT).add(majorMatchScore);

        var base = jpaQueryFactory
                .select(user)
                .from(user)
                .leftJoin(user.interests, interest);

        if (!tags.isEmpty()) {
            base.on(interest.in(tags));
        } else {
            base.on(Expressions.booleanTemplate("1=0"));
        }

        List<UserEntity> content = base
                .where(where)
                .groupBy(user.id)
                .orderBy(
                        score.desc(),
                        user.lastModifiedAt.desc().nullsLast(),
                        user.createdAt.desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(user.id.countDistinct())
                .from(user)
                .where(where)
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0L : total);
    }
}
