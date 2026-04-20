package project.team.ondo.domain.community.post.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.team.ondo.domain.community.post.data.request.SearchPostRequest;
import project.team.ondo.domain.community.post.data.response.PostRecommendItemResponse;

public interface PostSearchQueryRepository {
    Page<@NonNull PostRecommendItemResponse> search(SearchPostRequest request, Pageable pageable);
}
