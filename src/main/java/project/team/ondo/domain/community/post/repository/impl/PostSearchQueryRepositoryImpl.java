package project.team.ondo.domain.community.post.repository.impl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.team.ondo.domain.community.post.constant.PostStatus;
import project.team.ondo.domain.community.post.data.request.SearchPostRequest;
import project.team.ondo.domain.community.post.data.response.PostRecommendItemResponse;
import project.team.ondo.domain.community.post.entity.PostEntity;
import project.team.ondo.domain.community.post.entity.QPostEntity;
import project.team.ondo.domain.community.post.repository.PostSearchQueryRepository;
import project.team.ondo.domain.user.constant.UserStatus;
import project.team.ondo.domain.user.entity.QUserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostSearchQueryRepositoryImpl implements PostSearchQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private final QPostEntity post = QPostEntity.postEntity;
    private final QUserEntity user = QUserEntity.userEntity;

    @Override
    public Page<@NonNull PostRecommendItemResponse> search(
            SearchPostRequest request,
            Pageable pageable
    ) {
        StringPath tagPath = Expressions.stringPath("tag");

        StringPath keywordTag = Expressions.stringPath("keywordTag");

        BooleanExpression tagKeywordCondition = JPAExpressions.selectOne()
                .from(post.tags, keywordTag)
                .where(keywordTag.containsIgnoreCase(request.keyword()))
                .exists();

        BooleanExpression keywordCondition = post.title.containsIgnoreCase(request.keyword())
                .or(Expressions.stringTemplate("cast({0} as string)", post.content)
                        .containsIgnoreCase(request.keyword()))
                .or(tagKeywordCondition);

        List<String> filterTags = request.normalizedTags();

        StringPath subTag = Expressions.stringPath("subTag");

        BooleanExpression tagFilter = filterTags.isEmpty()
                ? null
                : JPAExpressions.selectOne()
                        .from(post.tags, subTag)
                        .where(subTag.lower().in(filterTags))
                        .exists();

        OrderSpecifier<?>[] orderBy = request.isLatest()
                ? new OrderSpecifier[]{post.createdAt.desc()}
                : new OrderSpecifier[]{post.likeCount.desc(), post.viewCount.desc()};

        List<Long> postIds = jpaQueryFactory
                .select(post.id)
                .from(post)
                .join(post.author, user)
                .where(
                        post.status.eq(PostStatus.ACTIVE),
                        user.status.eq(UserStatus.ACTIVE),
                        keywordCondition,
                        tagFilter
                )
                .orderBy(orderBy)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if (postIds.isEmpty()) {
            return Page.empty(pageable);
        }

        List<PostEntity> posts = jpaQueryFactory
                .selectFrom(post)
                .join(post.author, user).fetchJoin()
                .leftJoin(post.tags, tagPath).fetchJoin()
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
                        p.getCommentCount(),
                        p.getCreatedAt()
                ))
                .toList();

        Long total = jpaQueryFactory
                .select(post.id.count())
                .from(post)
                .join(post.author, user)
                .where(
                        post.status.eq(PostStatus.ACTIVE),
                        user.status.eq(UserStatus.ACTIVE),
                        keywordCondition,
                        tagFilter
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0L : total);
    }
}