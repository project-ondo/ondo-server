package project.team.ondo.domain.chat.service;

import java.util.UUID;

public interface UnblockRoomService {
    void execute(UUID chatRoomId);
}
