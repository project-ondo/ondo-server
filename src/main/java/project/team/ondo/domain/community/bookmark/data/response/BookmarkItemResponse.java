package project.team.ondo.domain.community.bookmark.data.response;

import project.team.ondo.domain.community.bookmark.entity.BookmarkEntity;

import java.time.LocalDateTime;

public record BookmarkItemResponse(
        Long postId,
        String title,
        String authorName,
        long bookmarkCount,
        LocalDateTime bookmarkedAt
) {
    public static BookmarkItemResponse from(BookmarkEntity bookmark) {
        return new BookmarkItemResponse(
                bookmark.getPost().getId(),
                bookmark.getPost().getTitle(),
                bookmark.getPost().getAuthor().getDisplayName(),
                bookmark.getPost().getBookmarkCount(),
                bookmark.getCreatedAt()
        );
    }
}
