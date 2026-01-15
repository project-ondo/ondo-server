package project.team.ondo.domain.chat.service;

import java.util.UUID;

public interface MarkRoomReadService {
    void execute(UUID chatRoomId, Long lastReadMessageId);
}
