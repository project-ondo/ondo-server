package project.team.ondo.domain.chat.service;

import lombok.NonNull;
import project.team.ondo.domain.chat.data.response.ChatMessageResponse;
import project.team.ondo.global.response.CursorResponse;

import java.util.UUID;

public interface GetRoomMessagesService {
    CursorResponse<@NonNull ChatMessageResponse> execute(UUID roomPublicId, Long cursor, int size);
}
