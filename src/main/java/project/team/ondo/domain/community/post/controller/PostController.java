package project.team.ondo.domain.community.post.controller;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.team.ondo.domain.community.post.data.request.CreatePostRequest;
import project.team.ondo.domain.community.post.data.request.UpdatePostRequest;
import project.team.ondo.domain.community.post.data.response.PostDetailResponse;
import project.team.ondo.domain.community.post.data.response.PostRecommendItemResponse;
import project.team.ondo.domain.community.post.service.*;
import project.team.ondo.global.response.ApiResponse;
import project.team.ondo.global.response.PageResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final RecommendPostService recommendPostService;
    private final CreatePostService createPostService;
    private final UpdatePostService updatePostService;
    private final GetPostDetailService getPostDetailService;
    private final DeletePostService deletePostService;

    @GetMapping("/recommend")
    public ResponseEntity<@NonNull ApiResponse<PageResponse<PostRecommendItemResponse>>> recommendPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "추천 게시물 조회에 성공했습니다.",
                        PageResponse.from(recommendPostService.execute(pageable))
                )
        );
    }

    @PostMapping
    public ResponseEntity<@NonNull ApiResponse<Long>> createPost(
            @Valid @RequestBody CreatePostRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "게시물 생성에 성공했습니다.",
                        createPostService.execute(request)
                )
        );
    }

    @GetMapping("/{postId}")
    public ResponseEntity<@NonNull ApiResponse<PostDetailResponse>> getPostDetail(
            @PathVariable Long postId
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "게시물 상세 조회에 성공했습니다.",
                        getPostDetailService.execute(postId)
                )
        );
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<@NonNull ApiResponse<Void>> updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody UpdatePostRequest request
    ) {
        updatePostService.execute(postId, request);
        return ResponseEntity.ok(
                ApiResponse.success("게시물 수정에 성공했습니다.")
        );
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<@NonNull ApiResponse<Void>> deletePost(
            @PathVariable Long postId
    ) {
        deletePostService.execute(postId);
        return ResponseEntity.ok(
                ApiResponse.success("게시물 삭제에 성공했습니다.")
        );
    }

}
