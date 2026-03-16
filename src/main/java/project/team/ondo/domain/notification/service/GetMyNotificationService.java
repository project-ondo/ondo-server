package project.team.ondo.domain.notification.service;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.team.ondo.domain.notification.data.response.NotificationItemResponse;
import project.team.ondo.domain.user.entity.UserEntity;

public interface GetMyNotificationService {
    Page<@NonNull NotificationItemResponse> execute(UserEntity me, Pageable pageable);
}
