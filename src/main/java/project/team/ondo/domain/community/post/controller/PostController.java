package project.team.ondo.domain.community.post.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.team.ondo.domain.community.post.data.response.PostRecommendItemResponse;
import project.team.ondo.domain.community.post.service.RecommendPostService;
import project.team.ondo.global.response.ApiResponse;
import project.team.ondo.global.response.PageResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final RecommendPostService recommendPostService;

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
}
