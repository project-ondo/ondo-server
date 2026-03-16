package project.team.ondo.domain.user.service;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.team.ondo.domain.user.data.request.UserSearchCondition;
import project.team.ondo.domain.user.data.response.UserRecommendItemResponse;

public interface SearchUserService {
    Page<@NonNull UserRecommendItemResponse> execute(UserSearchCondition condition, Pageable pageable);
}
