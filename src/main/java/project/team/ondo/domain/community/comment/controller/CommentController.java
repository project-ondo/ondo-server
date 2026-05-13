package project.team.ondo.domain.community.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.global.controller.BaseApiController;
import project.team.ondo.global.response.ApiResponse;
import project.team.ondo.global.response.PageResponse;
import project.team.ondo.global.security.annotation.CurrentUser;

@Tag(name = "Comment", description = "댓글")
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController extends BaseApiController {

    private final CreateCommentService createCommentService;
    private final GetCommentsService getCommentsService;
    private final DeleteCommentService deleteCommentService;

    @Operation(summary = "댓글 작성")
    @PostMapping("/{postId}")
    public ResponseEntity<@NonNull ApiResponse<Void>> create(
            @CurrentUser UserEntity me,
            @Parameter(description = "게시글 ID") @PathVariable Long postId,
            @Valid @RequestBody CreateCommentRequest request
    ) {
        createCommentService.execute(me, postId, request);
        return ok("댓글 작성에 성공했습니다.");
    }

    @Operation(summary = "댓글 목록 조회")
    @GetMapping("/{postId}")
    public ResponseEntity<@NonNull ApiResponse<PageResponse<CommentItemResponse>>> getComments(
            @Parameter(description = "게시글 ID") @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));
        return ok("댓글 조회에 성공했습니다.", PageResponse.from(getCommentsService.execute(postId, pageable)));
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<@NonNull ApiResponse<Void>> delete(
            @CurrentUser UserEntity me,
            @Parameter(description = "댓글 ID") @PathVariable Long commentId
    ) {
        deleteCommentService.execute(me, commentId);
        return ok("댓글 삭제에 성공했습니다.");
    }
}
