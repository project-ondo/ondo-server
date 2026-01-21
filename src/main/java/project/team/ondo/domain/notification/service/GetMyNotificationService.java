package project.team.ondo.domain.notification.service;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.team.ondo.domain.notification.data.response.NotificationItemResponse;

public interface GetMyNotificationService {
    Page<@NonNull NotificationItemResponse> execute(Pageable pageable);
}
