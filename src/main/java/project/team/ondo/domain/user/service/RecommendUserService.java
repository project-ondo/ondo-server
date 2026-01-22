package project.team.ondo.domain.user.service;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.team.ondo.domain.user.data.response.UserRecommendItemResponse;

public interface RecommendUserService {
    Page<@NonNull UserRecommendItemResponse> execute(Pageable pageable);
}
