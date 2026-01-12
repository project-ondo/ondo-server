package project.team.ondo.domain.chat.service;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.team.ondo.domain.chat.data.response.ChatMessageResponse;

import java.util.UUID;

public interface GetRoomMessagesService {
    Page<@NonNull ChatMessageResponse> execute(UUID roomId, Pageable pageable);
}
