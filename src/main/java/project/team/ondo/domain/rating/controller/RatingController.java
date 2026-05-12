package project.team.ondo.domain.rating.controller;

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

@RestController
@RequiredArgsConstructor
@RequestMapping("/rating")
public class RatingController extends BaseApiController {

    private final RateUserService rateUserService;
    private final GetMyRatingsService getMyRatingsService;
    private final GetUserRatingsService getUserRatingsService;

    @PostMapping("/rooms/{chatRoomPublicId}")
    public ResponseEntity<@NonNull ApiResponse<Void>> rateUser(
            @CurrentUser UserEntity me,
            @PathVariable UUID chatRoomPublicId,
            @Valid @RequestBody RateUserRequest request
    ) {
        rateUserService.execute(me, chatRoomPublicId, request.stars(), request.comment());
        return ok("별점 등록에 성공했습니다.");
    }

    @GetMapping("/me")
    public ResponseEntity<@NonNull ApiResponse<CursorResponse<UserRatingResponse>>> getMyRatings(
            @CurrentUser UserEntity me,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ok("평가 목록 조회에 성공했습니다.", getMyRatingsService.execute(me, cursor, size));
    }

    @GetMapping("/users/{userPublicId}")
    public ResponseEntity<@NonNull ApiResponse<CursorResponse<UserRatingResponse>>> getUserRatings(
            @PathVariable UUID userPublicId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ok("평가 목록 조회에 성공했습니다.", getUserRatingsService.execute(userPublicId, cursor, size));
    }
}
