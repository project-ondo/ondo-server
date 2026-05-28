package project.team.ondo.domain.community.post.repository;

import project.team.ondo.domain.community.post.cache.PopularPostCacheItem;

import java.util.List;

public interface PopularPostQueryRepository {
    List<PopularPostCacheItem> fetchTop10ByRecentLikes();
}
