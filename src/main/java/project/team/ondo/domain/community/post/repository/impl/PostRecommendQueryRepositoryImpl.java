package project.team.ondo.domain.community.post.repository.impl;

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
import project.team.ondo.domain.community.post.constant.PostStatus;
import project.team.ondo.domain.community.post.data.response.PostRecommendItemResponse;
import project.team.ondo.domain.community.post.entity.PostEntity;
import project.team.ondo.domain.community.post.entity.QPostEntity;
import project.team.ondo.domain.community.post.repository.PostRecommendQueryRepository;
import project.team.ondo.domain.user.constant.UserStatus;
import project.team.ondo.domain.user.entity.QUserEntity;
import project.team.ondo.domain.user.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static project.team.ondo.domain.user.constant.RecommendationWeights.INTEREST_WEIGHT;
import static project.team.ondo.domain.user.constant.RecommendationWeights.MAJOR_WEIGHT;

@Repository
@RequiredArgsConstructor
public class PostRecommendQueryRepositoryImpl implements PostRecommendQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private static final long LIKE_WEIGHT = 1L;
    private static final long COMMENT_WEIGHT = 2L;

    private final QUserEntity user = QUserEntity.userEntity;
    private final QPostEntity post = QPostEntity.postEntity;

    @Override
    public Page<@NonNull PostRecommendItemResponse> recommend(
            UserEntity me,
            Pageable pageable
    ) {

        List<String> interests = me.getInterests() == null
                ? List.of()
                : me.getInterests().stream()
                .filter(s -> s != null && !s.isBlank())
                .map(String::trim)
                .toList();

        StringPath tag = Expressions.stringPath("tag");

        NumberExpression<Long> interestScore =
                interests.isEmpty()
                        ? Expressions.numberTemplate(Long.class, "0")
                        : tag.count().multiply(INTEREST_WEIGHT);

        NumberExpression<Long> majorScore =
                new CaseBuilder()
                        .when(post.author.major.eq(me.getMajor()))
                        .then(MAJOR_WEIGHT)
                        .otherwise(0L);

        NumberExpression<Long> activityScore =
                post.likeCount.multiply(LIKE_WEIGHT)
                        .add(post.commentCount.multiply(COMMENT_WEIGHT));

        NumberExpression<Long> totalScore =
                interestScore.add(majorScore).add(activityScore);

        List<Long> postIds = jpaQueryFactory
                .select(post.id)
                .from(post)
                .join(post.author, user)
                .leftJoin(post.tags, tag)
                .where(
                        post.status.eq(PostStatus.ACTIVE),
                        post.author.status.eq(UserStatus.ACTIVE)
                )
                .groupBy(post.id)
                .orderBy(
                        totalScore.desc(),
                        post.createdAt.desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if (postIds.isEmpty()) {
            return Page.empty(pageable);
        }

        List<PostEntity> posts = jpaQueryFactory
                .selectFrom(post)
                .join(post.author, user).fetchJoin()
                .leftJoin(post.tags, tag).fetchJoin()
                .where(post.id.in(postIds))
                .distinct()
                .fetch();

        Map<Long, PostEntity> postMap = posts.stream()
                .collect(Collectors.toMap(PostEntity::getId, p -> p));

        List<PostRecommendItemResponse> content = postIds.stream()
                .map(postMap::get)
                .filter(Objects::nonNull)
                .map(p -> new PostRecommendItemResponse(
                        p.getId(),
                        p.getTitle(),
                        p.getAuthor().getDisplayName(),
                        new ArrayList<>(p.getTags()),
                        p.getViewCount(),
                        p.getLikeCount(),
                        p.getCommentCount()
                ))
                .toList();

        Long total = jpaQueryFactory
                .select(post.id.count())
                .from(post)
                .where(post.status.eq(PostStatus.ACTIVE))
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0L : total);
    }
}
