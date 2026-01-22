package project.team.ondo.domain.notification.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.team.ondo.domain.notification.data.request.UpdateMyNotificationSettingRequest;
import project.team.ondo.domain.notification.data.response.MyNotificationSettingResponse;
import project.team.ondo.domain.notification.service.GetMyNotificationSettingService;
import project.team.ondo.domain.notification.service.UpdateMyNotificationSettingService;
import project.team.ondo.global.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification-settings")
public class NotificationSettingController {

    private final GetMyNotificationSettingService getMyNotificationSettingService;
    private final UpdateMyNotificationSettingService updateMyNotificationSettingService;

    @GetMapping("/my")
    public ResponseEntity<@NonNull ApiResponse<MyNotificationSettingResponse>> getMySetting() {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "내 알림 설정 조회에 성공했습니다.",
                        getMyNotificationSettingService.execute()
                )
        );
    }

    @PatchMapping("/my")
    public ResponseEntity<@NonNull ApiResponse<Void>> updateMy(@RequestBody UpdateMyNotificationSettingRequest request) {
        updateMyNotificationSettingService.execute(request);
        return ResponseEntity.ok(ApiResponse.success("알림 설정 변경에 성공했습니다."));
    }
}
