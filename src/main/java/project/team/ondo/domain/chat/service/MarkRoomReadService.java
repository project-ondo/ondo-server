package project.team.ondo.domain.chat.service;

import project.team.ondo.domain.user.entity.UserEntity;

import java.util.UUID;

public interface MarkRoomReadService {
    void execute(UserEntity me, UUID chatRoomId, Long lastReadMessageId);
    void execute(UUID readerPublicId, UUID chatRoomPublicId, Long lastReadMessageId);
}
