package project.team.ondo.domain.chat.repository;

import java.util.UUID;

public interface ChatRoomMemberCommandRepository {
    int increaseUnreadCount(long roomId, long userId, long delta);
    int resetUnreadCount(long roomId, long userId);
    int updateLastReadMessageIdMax(long roomId, long userId, long next);
    boolean isMuted(UUID chatRoomPublicId, long userId);
    void mute(UUID chatRoomPublicId, long userId);
    void unmute(UUID chatRoomPublicId, long userId);
}
