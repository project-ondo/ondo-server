package project.team.ondo.domain.chat.service;

import java.util.UUID;

public interface UnmuteChatRoomService {
    void execute(UUID chatRoomPublicId);
}
