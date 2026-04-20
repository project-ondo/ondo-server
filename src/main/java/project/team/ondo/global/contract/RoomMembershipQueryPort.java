package project.team.ondo.global.contract;

import java.util.UUID;

public interface RoomMembershipQueryPort {
    RoomMembershipResult query(UUID chatRoomPublicId, Long userId);
}
