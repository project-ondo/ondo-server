package project.team.ondo.domain.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.team.ondo.domain.notification.data.response.NotificationItemResponse;
import project.team.ondo.domain.notification.service.CountUnreadNotificationService;
import project.team.ondo.domain.notification.service.DeleteReadNotificationsService;
import project.team.ondo.domain.notification.service.GetMyNotificationService;
import project.team.ondo.domain.notification.service.ReadAllNotificationService;
import project.team.ondo.domain.notification.service.ReadNotificationService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.global.controller.BaseApiController;
import project.team.ondo.global.response.ApiResponse;
import project.team.ondo.global.response.PageResponse;
import project.team.ondo.global.security.annotation.CurrentUser;

@Tag(name = "Notification", description = "알림")
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController extends BaseApiController {

    private final CountUnreadNotificationService countUnreadNotificationService;
    private final DeleteReadNotificationsService deleteReadNotificationsService;
    private final GetMyNotificationService getMyNotificationService;
    private final ReadNotificationService readNotificationService;
    private final ReadAllNotificationService readAllNotificationService;

    @Operation(summary = "내 알림 목록 조회")
    @GetMapping
    public ResponseEntity<@NonNull ApiResponse<PageResponse<@NonNull NotificationItemResponse>>> getMyNotifications(
            @CurrentUser UserEntity me,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ok("알림 목록 조회에 성공했습니다.", PageResponse.from(getMyNotificationService.execute(me, pageable)));
    }

    @Operation(summary = "미읽음 알림 개수 조회")
    @GetMapping("/unread/count")
    public ResponseEntity<@NonNull ApiResponse<Long>> countUnread(@CurrentUser UserEntity me) {
        return ok("미읽음 알림 개수 조회에 성공했습니다.", countUnreadNotificationService.execute(me));
    }

    @Operation(summary = "알림 읽음 처리")
    @PostMapping("/{notificationId}/read")
    public ResponseEntity<@NonNull ApiResponse<Void>> read(
            @CurrentUser UserEntity me,
            @Parameter(description = "알림 ID") @PathVariable Long notificationId
    ) {
        readNotificationService.execute(me, notificationId);
        return ok("알림 읽음 처리에 성공했습니다.");
    }

    @Operation(summary = "알림 전체 읽음 처리")
    @PostMapping("/read/all")
    public ResponseEntity<@NonNull ApiResponse<Long>> readAll(@CurrentUser UserEntity me) {
        return ok("알림 전체 읽음 처리에 성공했습니다.", readAllNotificationService.execute(me));
    }

    @Operation(summary = "읽은 알림 전체 삭제")
    @DeleteMapping("/read")
    public ResponseEntity<@NonNull ApiResponse<Long>> deleteReadAll(@CurrentUser UserEntity me) {
        return ok("읽은 알림 전체 삭제에 성공했습니다.", deleteReadNotificationsService.execute(me));
    }
}
