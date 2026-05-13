package project.team.ondo.domain.community.bookmark.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.community.bookmark.data.response.BookmarkItemResponse;
import project.team.ondo.domain.community.bookmark.repository.BookmarkRepository;
import project.team.ondo.domain.community.bookmark.service.GetMyBookmarksService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.global.response.PageResponse;

@Service
@RequiredArgsConstructor
public class GetMyBookmarksServiceImpl implements GetMyBookmarksService {

    private final BookmarkRepository bookmarkRepository;

    @Transactional(readOnly = true)
    @Override
    public PageResponse<BookmarkItemResponse> execute(UserEntity me, Pageable pageable) {
        Pageable sorted = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
        return PageResponse.from(
                bookmarkRepository.findAllByUserWithPost(me, sorted)
                        .map(BookmarkItemResponse::from)
        );
    }
}
