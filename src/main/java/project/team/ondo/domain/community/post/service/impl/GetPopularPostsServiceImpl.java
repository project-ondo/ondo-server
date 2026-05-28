package project.team.ondo.domain.community.post.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.community.post.cache.PopularPostCacheItem;
import project.team.ondo.domain.community.post.cache.PopularPostCacheRepository;
import project.team.ondo.domain.community.post.constant.PostStatus;
import project.team.ondo.domain.community.post.data.response.PopularPostResponse;
import project.team.ondo.domain.community.post.entity.PostEntity;
import project.team.ondo.domain.community.post.repository.PostRepository;
import project.team.ondo.domain.community.post.service.GetPopularPostsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetPopularPostsServiceImpl implements GetPopularPostsService {

    private final PopularPostCacheRepository popularPostCacheRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    @Override
    public List<PopularPostResponse> execute() {
        List<PopularPostCacheItem> cachedItems = popularPostCacheRepository.findAll();
        if (cachedItems.isEmpty()) {
            return List.of();
        }

        List<Long> postIds = cachedItems.stream()
                .map(PopularPostCacheItem::postId)
                .toList();

        List<PostEntity> posts = postRepository.findAllByIdWithDetails(postIds);
        Map<Long, PostEntity> postMap = posts.stream()
                .collect(Collectors.toMap(PostEntity::getId, p -> p));

        List<PopularPostResponse> result = new ArrayList<>();
        int rank = 1;
        for (PopularPostCacheItem item : cachedItems) {
            PostEntity post = postMap.get(item.postId());
            if (post != null && post.getStatus() == PostStatus.ACTIVE) {
                result.add(PopularPostResponse.from(rank++, post));
            }
        }
        return result;
    }
}
