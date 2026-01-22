package project.team.ondo.domain.community.post.data.response;

import java.util.List;

public record PostRecommendItemResponse(
        Long postId,
        String title,
        String authorName,
        List<String> tags,
        long viewCount,
        long likeCount,
        long commentCount
) {
}
