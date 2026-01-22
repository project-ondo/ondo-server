package project.team.ondo.domain.chat.repository;

import java.util.UUID;

public interface ChatRoomMuteRepository {
    boolean isMuted(UUID chatRoomPublicId, long userId);
    void mute(UUID chatRoomPublicId, long userId);
    void unmute(UUID chatRoomPublicId, long userId);
}
