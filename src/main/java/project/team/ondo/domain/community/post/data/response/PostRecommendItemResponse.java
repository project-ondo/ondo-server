package project.team.ondo.domain.community.post.data.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PostRecommendItemResponse(
        Long postId,
        UUID userPublicId,
        String title,
        String authorName,
        List<String> tags,
        long viewCount,
        long likeCount,
        long commentCount,
        long bookmarkCount,
        LocalDateTime createdAt
) {
}
