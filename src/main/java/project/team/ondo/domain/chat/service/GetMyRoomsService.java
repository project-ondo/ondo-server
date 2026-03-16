package project.team.ondo.domain.chat.service;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.team.ondo.domain.chat.data.response.ChatRoomListItemResponse;
import project.team.ondo.domain.user.entity.UserEntity;

public interface GetMyRoomsService {
    Page<@NonNull ChatRoomListItemResponse> execute(UserEntity me, Pageable pageable);
}
