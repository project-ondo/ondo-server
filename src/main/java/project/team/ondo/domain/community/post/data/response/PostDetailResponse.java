package project.team.ondo.domain.community.post.data.response;

import project.team.ondo.domain.community.post.entity.PostEntity;

import java.util.List;

public record PostDetailResponse(
        Long postId,
        String title,
        String content,
        String authorName,
        List<String> tags,
        Long viewCount,
        Long likeCount,
        Long commentCount
) {
    public static PostDetailResponse from(PostEntity post) {
        return new PostDetailResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor().getDisplayName(),
                post.getTags(),
                post.getViewCount(),
                post.getLikeCount(),
                post.getCommentCount()
        );
    }
}
