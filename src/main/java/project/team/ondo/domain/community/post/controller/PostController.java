package project.team.ondo.domain.community.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import project.team.ondo.domain.community.post.data.response.PopularPostResponse;
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

@Tag(name = "Post", description = "커뮤니티 게시글")
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController extends BaseApiController {

    private final GetPopularPostsService getPopularPostsService;
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

    @Operation(summary = "인기 게시글 Top 10 조회")
    @GetMapping("/popular")
    public ResponseEntity<@NonNull ApiResponse<List<PopularPostResponse>>> getPopularPosts() {
        return ok("인기 게시물 조회에 성공했습니다.", getPopularPostsService.execute());
    }

    @Operation(summary = "추천 게시글 목록 조회")
    @GetMapping("/recommend")
    public ResponseEntity<@NonNull ApiResponse<PageResponse<PostRecommendItemResponse>>> recommendPosts(
            @CurrentUser UserEntity me,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ok("추천 게시물 조회에 성공했습니다.", PageResponse.from(recommendPostService.execute(me, pageable)));
    }

    @Operation(summary = "게시글 검색")
    @GetMapping("/search")
    public ResponseEntity<@NonNull ApiResponse<PageResponse<PostRecommendItemResponse>>> searchPosts(
            @Valid @ModelAttribute SearchPostRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ok("게시물 검색에 성공했습니다.", PageResponse.from(searchPostService.execute(request, pageable)));
    }

    @Operation(summary = "게시글 작성")
    @PostMapping
    public ResponseEntity<@NonNull ApiResponse<Long>> createPost(
            @CurrentUser UserEntity me,
            @Valid @RequestBody CreatePostRequest request
    ) {
        return ok("게시물 생성에 성공했습니다.", createPostService.execute(me, request));
    }

    @Operation(summary = "게시글 상세 조회")
    @GetMapping("/{postId}")
    public ResponseEntity<@NonNull ApiResponse<PostDetailResponse>> getPostDetail(
            @Parameter(description = "게시글 ID") @PathVariable Long postId
    ) {
        return ok("게시물 상세 조회에 성공했습니다.", getPostDetailService.execute(postId));
    }

    @Operation(summary = "게시글 수정")
    @PatchMapping("/{postId}")
    public ResponseEntity<@NonNull ApiResponse<Void>> updatePost(
            @CurrentUser UserEntity me,
            @Parameter(description = "게시글 ID") @PathVariable Long postId,
            @Valid @RequestBody UpdatePostRequest request
    ) {
        updatePostService.execute(me, postId, request);
        return ok("게시물 수정에 성공했습니다.");
    }

    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/{postId}")
    public ResponseEntity<@NonNull ApiResponse<Void>> deletePost(
            @CurrentUser UserEntity me,
            @Parameter(description = "게시글 ID") @PathVariable Long postId
    ) {
        deletePostService.execute(me, postId);
        return ok("게시물 삭제에 성공했습니다.");
    }

    @Operation(summary = "게시글 좋아요")
    @PostMapping("/{postId}/like")
    public ResponseEntity<@NonNull ApiResponse<Void>> likePost(
            @CurrentUser UserEntity me,
            @Parameter(description = "게시글 ID") @PathVariable Long postId
    ) {
        likePostService.execute(me, postId);
        return ok("게시물 좋아요에 성공했습니다.");
    }

    @Operation(summary = "게시글 좋아요 취소")
    @DeleteMapping("/{postId}/like")
    public ResponseEntity<@NonNull ApiResponse<Void>> unlikePost(
            @CurrentUser UserEntity me,
            @Parameter(description = "게시글 ID") @PathVariable Long postId
    ) {
        unlikePostService.execute(me, postId);
        return ok("게시물 좋아요 취소에 성공했습니다.");
    }

    @Operation(summary = "게시글 북마크 추가")
    @PostMapping("/{postId}/bookmark")
    public ResponseEntity<@NonNull ApiResponse<Void>> saveBookmark(
            @CurrentUser UserEntity me,
            @Parameter(description = "게시글 ID") @PathVariable Long postId
    ) {
        saveBookmarkService.execute(me, postId);
        return ok("게시물 북마크에 성공했습니다.");
    }

    @Operation(summary = "게시글 북마크 해제")
    @DeleteMapping("/{postId}/bookmark")
    public ResponseEntity<@NonNull ApiResponse<Void>> removeBookmark(
            @CurrentUser UserEntity me,
            @Parameter(description = "게시글 ID") @PathVariable Long postId
    ) {
        removeBookmarkService.execute(me, postId);
        return ok("게시물 북마크 해제에 성공했습니다.");
    }

    @Operation(summary = "내 북마크 목록 조회")
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
