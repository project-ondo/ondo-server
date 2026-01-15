package project.team.ondo.domain.chat.service;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.team.ondo.domain.chat.data.response.ChatRoomListItemResponse;

public interface GetMyRoomsService {
    Page<@NonNull ChatRoomListItemResponse> execute(Pageable pageable);
}
