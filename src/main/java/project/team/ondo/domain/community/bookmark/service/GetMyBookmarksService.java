package project.team.ondo.domain.community.bookmark.service;

import org.springframework.data.domain.Pageable;
import project.team.ondo.domain.community.bookmark.data.response.BookmarkItemResponse;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.global.response.PageResponse;

public interface GetMyBookmarksService {
    PageResponse<BookmarkItemResponse> execute(UserEntity me, Pageable pageable);
}
