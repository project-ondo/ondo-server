package project.team.ondo.domain.chat.repository;

public interface ChatRoomMemberCommandRepository {
    int increaseUnreadCount(long roomId, long userId, long delta);
    int resetUnreadCount(long roomId, long userId);
    int updateLastReadMessageIdMax(long roomId, long userId, long next);
}
