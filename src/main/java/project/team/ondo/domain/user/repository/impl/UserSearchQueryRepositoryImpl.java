package project.team.ondo.domain.user.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.team.ondo.domain.user.constant.UserStatus;
import project.team.ondo.domain.user.data.request.UserSearchCondition;
import project.team.ondo.domain.user.entity.QUserEntity;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.repository.UserSearchQueryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSearchQueryRepositoryImpl implements UserSearchQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QUserEntity user = QUserEntity.userEntity;

    @Override
    public Page<@NonNull UserEntity> searchUser(UserSearchCondition condition, Pageable pageable) {
        BooleanBuilder where = new BooleanBuilder();
        where.and(user.status.eq(UserStatus.ACTIVE));

        if (condition.keyword() != null && !condition.keyword().isBlank()) {
            String keyword = condition.keyword().trim();
            where.and(
                    user.displayName.containsIgnoreCase(keyword)
                            .or(user.bio.containsIgnoreCase(keyword))
                            .or(user.major.containsIgnoreCase(keyword))
            );
        }

        if (condition.major() != null && !condition.major().isBlank()) {
            where.and(user.major.eq(condition.major().trim()));
        }

        List<String> tags = null;
        if (condition.interests() != null && !condition.interests().isEmpty()) {
            tags = condition.interests().stream()
                    .filter(str -> str != null && !str.isBlank())
                    .map(String::trim)
                    .toList();

            if (!tags.isEmpty()) {
                where.and(user.interests.any().in(tags));
            }
        }

        String sort = condition.sort() == null ? "" : condition.sort().trim().toLowerCase();

        List<UserEntity> content;

        if ("relevance".equals(sort) && tags != null && !tags.isEmpty()) {
            StringPath interest = Expressions.stringPath("interest");
            NumberExpression<Long> matchCount = interest.count();

            content = jpaQueryFactory
                    .select(user)
                    .from(user)
                    .leftJoin(user.interests, interest)
                    .on(interest.in(tags))
                    .where(where)
                    .groupBy(user.id)
                    .orderBy(
                            matchCount.desc(),
                            user.lastModifiedAt.desc().nullsLast(),
                            user.createdAt.desc()
                    )
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

        } else {
            var query = jpaQueryFactory
                    .selectFrom(user)
                    .where(where)
                    .distinct();

            if ("latest".equals(sort)) {
                query.orderBy(user.lastModifiedAt.desc().nullsLast(), user.createdAt.desc());
            } else {
                query.orderBy(user.displayName.asc(), user.createdAt.desc());
            }

            content = query
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
        }

        Long total = jpaQueryFactory
                .select(user.id.countDistinct())
                .from(user)
                .where(where)
                .fetchOne();

        long totalElements = total == null ? 0L : total;

        return new PageImpl<>(content, pageable, totalElements);
    }
}
