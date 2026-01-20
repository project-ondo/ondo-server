package project.team.ondo.domain.notification.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.team.ondo.domain.notification.data.response.NotificationItemResponse;
import project.team.ondo.domain.notification.service.CountUnreadNotificationService;
import project.team.ondo.domain.notification.service.GetMyNotificationService;
import project.team.ondo.domain.notification.service.ReadAllNotificationService;
import project.team.ondo.domain.notification.service.ReadNotificationService;
import project.team.ondo.global.response.ApiResponse;
import project.team.ondo.global.response.PageResponse;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final CountUnreadNotificationService countUnreadNotificationService;
    private final GetMyNotificationService getMyNotificationService;
    private final ReadNotificationService readNotificationService;
    private final ReadAllNotificationService readAllNotificationService;

    @GetMapping
    public ResponseEntity<@NonNull ApiResponse<PageResponse<@NonNull NotificationItemResponse>>> getMyNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        var resultPage = getMyNotificationService.execute(pageable);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "알림 목록 조회에 성공했습니다.",
                        PageResponse.from(resultPage)
                )
        );
    }

    @GetMapping("/unread/count")
    public ResponseEntity<@NonNull ApiResponse<Long>> countUnread() {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "미읽음 알림 개수 조회에 성공했습니다.",
                        countUnreadNotificationService.execute()
                )
        );
    }

    @PostMapping("/{notificationId}/read")
    public ResponseEntity<@NonNull ApiResponse<Void>> read(@PathVariable Long notificationId) {
        readNotificationService.execute(notificationId);
        return ResponseEntity.ok(ApiResponse.success("알림 읽음 처리에 성공했습니다."));
    }

    @PostMapping("/read/all")
    public ResponseEntity<@NonNull ApiResponse<Long>> readAll() {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "알림 전체 읽음 처리에 성공했습니다.",
                        readAllNotificationService.execute()
                )
        );
    }
}
