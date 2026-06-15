package project.team.ondo.domain.community.post.data.response;

import project.team.ondo.domain.community.post.entity.PostEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record PopularPostResponse(
        int rank,
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
    public static PopularPostResponse from(int rank, PostEntity post) {
        return new PopularPostResponse(
                rank,
                post.getId(),
                post.getAuthor().getPublicId(),
                post.getTitle(),
                post.getAuthor().getDisplayName(),
                new ArrayList<>(post.getTags()),
                post.getViewCount(),
                post.getLikeCount(),
                post.getCommentCount(),
                post.getBookmarkCount(),
                post.getCreatedAt()
        );
    }
}
