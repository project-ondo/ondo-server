package project.team.ondo.domain.community.post.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.community.post.data.request.SearchPostRequest;
import project.team.ondo.domain.community.post.data.response.PostRecommendItemResponse;

@Repository
public interface PostSearchQueryRepository {
    Page<@NonNull PostRecommendItemResponse> search(SearchPostRequest request, Pageable pageable);
}
