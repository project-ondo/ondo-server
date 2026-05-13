package project.team.ondo.domain.community.post.controller;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.team.ondo.domain.community.bookmark.data.response.BookmarkItemResponse;
import project.team.ondo.domain.community.bookmark.service.GetMyBookmarksService;
import project.team.ondo.domain.community.bookmark.service.RemoveBookmarkService;
import project.team.ondo.domain.community.bookmark.service.SaveBookmarkService;
import project.team.ondo.domain.community.post.data.request.CreatePostRequest;
import project.team.ondo.domain.community.post.data.request.SearchPostRequest;
import project.team.ondo.domain.community.post.data.request.UpdatePostRequest;
import project.team.ondo.domain.community.post.data.response.PostDetailResponse;
import project.team.ondo.domain.community.post.data.response.PostRecommendItemResponse;
import project.team.ondo.domain.community.post.service.*;
import project.team.ondo.domain.community.postlike.service.LikePostService;
import project.team.ondo.domain.community.postlike.service.UnlikePostService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.global.controller.BaseApiController;
import project.team.ondo.global.response.ApiResponse;
import project.team.ondo.global.response.PageResponse;
import project.team.ondo.global.security.annotation.CurrentUser;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController extends BaseApiController {

    private final RecommendPostService recommendPostService;
    private final CreatePostService createPostService;
    private final UpdatePostService updatePostService;
    private final GetPostDetailService getPostDetailService;
    private final DeletePostService deletePostService;
    private final LikePostService likePostService;
    private final UnlikePostService unlikePostService;
    private final SearchPostService searchPostService;
    private final SaveBookmarkService saveBookmarkService;
    private final RemoveBookmarkService removeBookmarkService;
    private final GetMyBookmarksService getMyBookmarksService;

    @GetMapping("/recommend")
    public ResponseEntity<@NonNull ApiResponse<PageResponse<PostRecommendItemResponse>>> recommendPosts(
            @CurrentUser UserEntity me,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ok("추천 게시물 조회에 성공했습니다.", PageResponse.from(recommendPostService.execute(me, pageable)));
    }

    @GetMapping("/search")
    public ResponseEntity<@NonNull ApiResponse<PageResponse<PostRecommendItemResponse>>> searchPosts(
            @Valid @ModelAttribute SearchPostRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ok("게시물 검색에 성공했습니다.", PageResponse.from(searchPostService.execute(request, pageable)));
    }

    @PostMapping
    public ResponseEntity<@NonNull ApiResponse<Long>> createPost(
            @CurrentUser UserEntity me,
            @Valid @RequestBody CreatePostRequest request
    ) {
        return ok("게시물 생성에 성공했습니다.", createPostService.execute(me, request));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<@NonNull ApiResponse<PostDetailResponse>> getPostDetail(
            @PathVariable Long postId
    ) {
        return ok("게시물 상세 조회에 성공했습니다.", getPostDetailService.execute(postId));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<@NonNull ApiResponse<Void>> updatePost(
            @CurrentUser UserEntity me,
            @PathVariable Long postId,
            @Valid @RequestBody UpdatePostRequest request
    ) {
        updatePostService.execute(me, postId, request);
        return ok("게시물 수정에 성공했습니다.");
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<@NonNull ApiResponse<Void>> deletePost(
            @CurrentUser UserEntity me,
            @PathVariable Long postId
    ) {
        deletePostService.execute(me, postId);
        return ok("게시물 삭제에 성공했습니다.");
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<@NonNull ApiResponse<Void>> likePost(
            @CurrentUser UserEntity me,
            @PathVariable Long postId
    ) {
        likePostService.execute(me, postId);
        return ok("게시물 좋아요에 성공했습니다.");
    }

    @DeleteMapping("/{postId}/like")
    public ResponseEntity<@NonNull ApiResponse<Void>> unlikePost(
            @CurrentUser UserEntity me,
            @PathVariable Long postId
    ) {
        unlikePostService.execute(me, postId);
        return ok("게시물 좋아요 취소에 성공했습니다.");
    }

    @PostMapping("/{postId}/bookmark")
    public ResponseEntity<@NonNull ApiResponse<Void>> saveBookmark(
            @CurrentUser UserEntity me,
            @PathVariable Long postId
    ) {
        saveBookmarkService.execute(me, postId);
        return ok("게시물 북마크에 성공했습니다.");
    }

    @DeleteMapping("/{postId}/bookmark")
    public ResponseEntity<@NonNull ApiResponse<Void>> removeBookmark(
            @CurrentUser UserEntity me,
            @PathVariable Long postId
    ) {
        removeBookmarkService.execute(me, postId);
        return ok("게시물 북마크 해제에 성공했습니다.");
    }

    @GetMapping("/bookmarks")
    public ResponseEntity<@NonNull ApiResponse<PageResponse<BookmarkItemResponse>>> getMyBookmarks(
            @CurrentUser UserEntity me,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ok("북마크 목록 조회에 성공했습니다.", getMyBookmarksService.execute(me, pageable));
    }
}
