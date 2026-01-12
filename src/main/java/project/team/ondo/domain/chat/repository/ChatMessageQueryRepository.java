package project.team.ondo.domain.chat.repository;

import project.team.ondo.domain.chat.entity.ChatMessageEntity;

import java.util.List;

public interface ChatMessageQueryRepository {
    List<ChatMessageEntity> findLastMessages(List<Long> roomIds);
    long countUnreadMessages(Long roomId, Long userId, Long lastReadMessageId);
}
