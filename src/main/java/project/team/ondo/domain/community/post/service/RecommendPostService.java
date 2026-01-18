package project.team.ondo.domain.community.post.service;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.team.ondo.domain.community.post.data.response.PostRecommendItemResponse;

public interface RecommendPostService {
    Page<@NonNull PostRecommendItemResponse> execute(Pageable pageable);
}
