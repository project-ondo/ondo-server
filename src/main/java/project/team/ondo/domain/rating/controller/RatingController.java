package project.team.ondo.domain.rating.controller;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.team.ondo.domain.rating.data.request.RateUserRequest;
import project.team.ondo.domain.rating.service.RateUserService;
import project.team.ondo.global.response.ApiResponse;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rating")
public class RatingController {

    private final RateUserService rateUserService;

    @PostMapping("/rooms/{chatRoomPublicId}")
    public ResponseEntity<@NonNull ApiResponse<Void>> rateUser(
            @PathVariable UUID chatRoomPublicId,
            @Valid @RequestBody RateUserRequest request
    ) {
        rateUserService.execute(chatRoomPublicId, request.stars(), request.comment());
        return ResponseEntity.ok(ApiResponse.success("별점 등록에 성공했습니다."));
    }
}
