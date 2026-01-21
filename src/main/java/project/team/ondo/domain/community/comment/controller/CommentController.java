package project.team.ondo.domain.community.comment.controller;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.team.ondo.domain.community.comment.data.request.CreateCommentRequest;
import project.team.ondo.domain.community.comment.data.response.CommentItemResponse;
import project.team.ondo.domain.community.comment.service.CreateCommentService;
import project.team.ondo.domain.community.comment.service.DeleteCommentService;
import project.team.ondo.domain.community.comment.service.GetCommentsService;
import project.team.ondo.global.response.ApiResponse;
import project.team.ondo.global.response.PageResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CreateCommentService createCommentService;
    private final GetCommentsService getCommentsService;
    private final DeleteCommentService deleteCommentService;

    @PostMapping("/{postId}")
    public ResponseEntity<@NonNull ApiResponse<Void>> create(
            @PathVariable Long postId,
            @Valid @RequestBody CreateCommentRequest request
    ) {
        createCommentService.execute(postId, request);
        return ResponseEntity.ok(
                ApiResponse.success("댓글 작성에 성공했습니다.")
        );
    }

    @GetMapping("/{postId}")
    public ResponseEntity<@NonNull ApiResponse<PageResponse<CommentItemResponse>>> getComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.ASC, "createdAt")
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "댓글 조회에 성공했습니다.",
                        PageResponse.from(
                                getCommentsService.execute(postId, pageable)
                        )
                )
        );
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<@NonNull ApiResponse<Void>> delete(
            @PathVariable Long commentId
    ) {
        deleteCommentService.execute(commentId);
        return ResponseEntity.ok(
                ApiResponse.success("댓글 삭제에 성공했습니다.")
        );
    }
}
