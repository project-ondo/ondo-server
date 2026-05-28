package project.team.ondo.domain.community.post.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.community.post.cache.PopularPostCacheItem;
import project.team.ondo.domain.community.post.constant.PostStatus;
import project.team.ondo.domain.community.post.entity.QPostEntity;
import project.team.ondo.domain.community.post.repository.PopularPostQueryRepository;
import project.team.ondo.domain.community.postlike.entity.QPostLikeEntity;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PopularPostQueryRepositoryImpl implements PopularPostQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private final QPostLikeEntity postLike = QPostLikeEntity.postLikeEntity;
    private final QPostEntity post = QPostEntity.postEntity;

    @Override
    public List<PopularPostCacheItem> fetchTop10ByRecentLikes() {
        var countExpr = postLike.id.count();

        List<Tuple> results = jpaQueryFactory
                .select(postLike.post.id, countExpr)
                .from(postLike)
                .join(postLike.post, post)
                .where(
                        postLike.createdAt.goe(LocalDateTime.now().minusDays(3)),
                        post.status.eq(PostStatus.ACTIVE)
                )
                .groupBy(postLike.post.id)
                .orderBy(countExpr.desc(), post.viewCount.desc())
                .limit(10)
                .fetch();

        return results.stream()
                .map(t -> new PopularPostCacheItem(t.get(postLike.post.id), t.get(countExpr)))
                .toList();
    }
}
