package project.team.ondo.domain.chat.service;

import java.util.UUID;

public interface MuteChatRoomService {
    void execute(UUID chatRoomPublicId);
}
