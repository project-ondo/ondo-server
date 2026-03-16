package project.team.ondo.domain.chat.service;

import lombok.NonNull;
import project.team.ondo.domain.chat.data.response.ChatMessageResponse;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.global.response.CursorResponse;

import java.util.UUID;

public interface GetRoomMessagesService {
    CursorResponse<@NonNull ChatMessageResponse> execute(UserEntity me, UUID roomPublicId, Long cursor, int size);
}
