package project.team.ondo.domain.chat.service;

import java.util.UUID;

public interface LeaveRoomService {
    void execute(UUID chatRoomId);
}
