package project.team.ondo.domain.user.data;

import project.team.ondo.domain.user.data.response.UserRecommendItemResponse;

import java.util.List;

public record UserRecommendCacheValue(
        List<UserRecommendItemResponse> items,
        long totalElements
) {
}
