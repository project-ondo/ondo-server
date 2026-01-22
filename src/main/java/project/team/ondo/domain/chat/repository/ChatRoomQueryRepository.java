package project.team.ondo.domain.chat.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.team.ondo.domain.chat.data.response.ChatRoomListItemResponse;

public interface ChatRoomQueryRepository {
    Page<@NonNull ChatRoomListItemResponse> findMyRooms(long meId, Pageable pageable);
}
