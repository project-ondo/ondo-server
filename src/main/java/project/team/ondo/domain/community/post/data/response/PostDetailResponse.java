package project.team.ondo.domain.community.post.data.response;

import project.team.ondo.domain.community.post.entity.PostEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record PostDetailResponse(
        Long postId,
        UUID userPublicId,
        String title,
        String content,
        String authorName,
        List<String> tags,
        Long viewCount,
        Long likeCount,
        Long commentCount,
        Long bookmarkCount,
        LocalDateTime createdAt
) {
    public static PostDetailResponse from(PostEntity post) {
        return new PostDetailResponse(
                post.getId(),
                post.getAuthor().getPublicId(),
                post.getTitle(),
                post.getContent(),
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
