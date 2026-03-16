package project.team.ondo.domain.rating.controller;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.team.ondo.domain.rating.data.request.RateUserRequest;
import project.team.ondo.domain.rating.service.RateUserService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.global.controller.BaseApiController;
import project.team.ondo.global.response.ApiResponse;
import project.team.ondo.global.security.annotation.CurrentUser;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rating")
public class RatingController extends BaseApiController {

    private final RateUserService rateUserService;

    @PostMapping("/rooms/{chatRoomPublicId}")
    public ResponseEntity<@NonNull ApiResponse<Void>> rateUser(
            @CurrentUser UserEntity me,
            @PathVariable UUID chatRoomPublicId,
            @Valid @RequestBody RateUserRequest request
    ) {
        rateUserService.execute(me, chatRoomPublicId, request.stars(), request.comment());
        return ok("별점 등록에 성공했습니다.");
    }
}
