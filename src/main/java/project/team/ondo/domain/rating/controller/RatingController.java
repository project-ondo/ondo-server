package project.team.ondo.domain.rating.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.team.ondo.domain.rating.data.request.RateUserRequest;
import project.team.ondo.domain.rating.data.response.UserRatingResponse;
import project.team.ondo.domain.rating.service.GetMyRatingsService;
import project.team.ondo.domain.rating.service.GetUserRatingsService;
import project.team.ondo.domain.rating.service.RateUserService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.global.controller.BaseApiController;
import project.team.ondo.global.response.ApiResponse;
import project.team.ondo.global.response.CursorResponse;
import project.team.ondo.global.security.annotation.CurrentUser;

import java.util.UUID;

@Tag(name = "Rating", description = "온도 평가")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rating")
public class RatingController extends BaseApiController {

    private final RateUserService rateUserService;
    private final GetMyRatingsService getMyRatingsService;
    private final GetUserRatingsService getUserRatingsService;

    @Operation(summary = "채팅방 상대방 별점 평가")
    @PostMapping("/rooms/{chatRoomPublicId}")
    public ResponseEntity<@NonNull ApiResponse<Void>> rateUser(
            @CurrentUser UserEntity me,
            @Parameter(description = "채팅방 publicId (UUID)") @PathVariable UUID chatRoomPublicId,
            @Valid @RequestBody RateUserRequest request
    ) {
        rateUserService.execute(me, chatRoomPublicId, request.stars(), request.comment());
        return ok("별점 등록에 성공했습니다.");
    }

    @Operation(summary = "내가 받은 평가 목록 조회 (커서 기반)")
    @GetMapping("/me")
    public ResponseEntity<@NonNull ApiResponse<CursorResponse<UserRatingResponse>>> getMyRatings(
            @CurrentUser UserEntity me,
            @Parameter(description = "커서 (마지막 조회 평가 ID, 첫 조회 시 생략)") @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ok("평가 목록 조회에 성공했습니다.", getMyRatingsService.execute(me, cursor, size));
    }

    @Operation(summary = "특정 유저가 받은 평가 목록 조회 (커서 기반)")
    @GetMapping("/users/{userPublicId}")
    public ResponseEntity<@NonNull ApiResponse<CursorResponse<UserRatingResponse>>> getUserRatings(
            @Parameter(description = "조회할 유저의 publicId (UUID)") @PathVariable UUID userPublicId,
            @Parameter(description = "커서 (마지막 조회 평가 ID, 첫 조회 시 생략)") @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ok("평가 목록 조회에 성공했습니다.", getUserRatingsService.execute(userPublicId, cursor, size));
    }
}