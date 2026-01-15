package project.team.ondo.domain.chat.repository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.chat.entity.ChatMessageEntity;
import project.team.ondo.domain.chat.entity.QChatMessageEntity;
import project.team.ondo.domain.chat.repository.ChatMessageQueryRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatMessageQueryRepositoryImpl implements ChatMessageQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QChatMessageEntity chatMessage = QChatMessageEntity.chatMessageEntity;

    @Override
    public List<ChatMessageEntity> findLastMessages(List<Long> roomIds) {
        if (roomIds == null || roomIds.isEmpty()) {
            return List.of();
        }

        var subQuery = JPAExpressions
                .select(chatMessage.id.max())
                .from(chatMessage)
                .where(chatMessage.roomId.in(roomIds))
                .groupBy(chatMessage.roomId);

        return jpaQueryFactory
                .selectFrom(chatMessage)
                .where(chatMessage.id.in(subQuery))
                .fetch();
    }

    @Override
    public long countUnreadMessages(Long roomId, Long userId, Long lastReadMessageId) {
        BooleanExpression where = chatMessage.roomId.eq(roomId)
                .and(chatMessage.senderId.ne(userId));

        if (lastReadMessageId != null) {
            where = where.and(chatMessage.id.gt(lastReadMessageId));
        }

        Long count = jpaQueryFactory
                .select(chatMessage.count())
                .from(chatMessage)
                .where(where)
                .fetchOne();

        return count == null ? 0L : count;
    }
}
