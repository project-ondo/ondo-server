package project.team.ondo.domain.notification.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import project.team.ondo.domain.notification.data.request.UpdateMyNotificationSettingRequest;
import project.team.ondo.domain.notification.data.response.MyNotificationSettingResponse;
import project.team.ondo.domain.notification.service.GetMyNotificationSettingService;
import project.team.ondo.domain.notification.service.UpdateMyNotificationSettingService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.global.controller.BaseApiController;
import project.team.ondo.global.response.ApiResponse;
import project.team.ondo.global.security.annotation.CurrentUser;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification-settings")
public class NotificationSettingController extends BaseApiController {

    private final GetMyNotificationSettingService getMyNotificationSettingService;
    private final UpdateMyNotificationSettingService updateMyNotificationSettingService;

    @GetMapping("/my")
    public ResponseEntity<@NonNull ApiResponse<MyNotificationSettingResponse>> getMySetting(@CurrentUser UserEntity me) {
        return ok("내 알림 설정 조회에 성공했습니다.", getMyNotificationSettingService.execute(me));
    }

    @PatchMapping("/my")
    public ResponseEntity<@NonNull ApiResponse<Void>> updateMy(
            @CurrentUser UserEntity me,
            @Valid @RequestBody UpdateMyNotificationSettingRequest request
    ) {
        updateMyNotificationSettingService.execute(me, request);
        return ok("알림 설정 변경에 성공했습니다.");
    }
}
