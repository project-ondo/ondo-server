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
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.global.controller.BaseApiController;
import project.team.ondo.global.response.ApiResponse;
import project.team.ondo.global.response.PageResponse;
import project.team.ondo.global.security.annotation.CurrentUser;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController extends BaseApiController {

    private final CreateCommentService createCommentService;
    private final GetCommentsService getCommentsService;
    private final DeleteCommentService deleteCommentService;

    @PostMapping("/{postId}")
    public ResponseEntity<@NonNull ApiResponse<Void>> create(
            @CurrentUser UserEntity me,
            @PathVariable Long postId,
            @Valid @RequestBody CreateCommentRequest request
    ) {
        createCommentService.execute(me, postId, request);
        return ok("댓글 작성에 성공했습니다.");
    }

    @GetMapping("/{postId}")
    public ResponseEntity<@NonNull ApiResponse<PageResponse<CommentItemResponse>>> getComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));
        return ok("댓글 조회에 성공했습니다.", PageResponse.from(getCommentsService.execute(postId, pageable)));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<@NonNull ApiResponse<Void>> delete(
            @CurrentUser UserEntity me,
            @PathVariable Long commentId
    ) {
        deleteCommentService.execute(me, commentId);
        return ok("댓글 삭제에 성공했습니다.");
    }
}
