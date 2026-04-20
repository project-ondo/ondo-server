package project.team.ondo.domain.community.post.service;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.team.ondo.domain.community.post.data.request.SearchPostRequest;
import project.team.ondo.domain.community.post.data.response.PostRecommendItemResponse;

public interface SearchPostService {
    Page<@NonNull PostRecommendItemResponse> execute(SearchPostRequest request, Pageable pageable);
}
