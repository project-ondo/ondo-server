package project.team.ondo.global.fcm.controller;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.team.ondo.global.fcm.data.request.DeactivateFcmTokenRequest;
import project.team.ondo.global.fcm.data.request.RegisterFcmTokenRequest;
import project.team.ondo.global.fcm.service.DeactivateAllFcmTokenService;
import project.team.ondo.global.fcm.service.DeactivateFcmTokenService;
import project.team.ondo.global.fcm.service.RegisterFcmTokenService;
import project.team.ondo.global.response.ApiResponse;

@RestController
@RequestMapping("/fcm")
@RequiredArgsConstructor
public class FcmTokenController {

    private final RegisterFcmTokenService registerFcmTokenService;
    private final DeactivateAllFcmTokenService deactivateAllFcmTokenService;
    private final DeactivateFcmTokenService deactivateFcmTokenService;

    @PostMapping("/token")
    public ResponseEntity<@NonNull ApiResponse<Void>> register(@Valid @RequestBody RegisterFcmTokenRequest request) {
        registerFcmTokenService.execute(request.token());
        return ResponseEntity.ok(
                ApiResponse.success(
                        "FCM 토큰 등록에 성공했습니다.")
        );
    }

    @DeleteMapping("/token")
    public ResponseEntity<@NonNull ApiResponse<Void>> deactivate(@Valid @RequestBody DeactivateFcmTokenRequest request) {
        deactivateFcmTokenService.execute(request.token());
        return ResponseEntity.ok(ApiResponse.success("FCM 토큰 비활성화에 성공했습니다."));
    }

    @DeleteMapping("/tokens")
    public ResponseEntity<@NonNull ApiResponse<Void>> deactivateAll() {
        deactivateAllFcmTokenService.execute();
        return ResponseEntity.ok(ApiResponse.success("모든 FCM 토큰 비활성화에 성공했습니다."));
    }
}
