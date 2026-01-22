package project.team.ondo.domain.chat.repository.impl;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.team.ondo.domain.chat.entity.QChatRoomMemberEntity;
import project.team.ondo.domain.chat.repository.ChatRoomMemberCommandRepository;

@Service
@RequiredArgsConstructor
public class ChatRoomMemberCommandRepositoryImpl implements ChatRoomMemberCommandRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QChatRoomMemberEntity chatRoomMember = QChatRoomMemberEntity.chatRoomMemberEntity;

    @Override
    public int increaseUnreadCount(long roomId, long userId, long delta) {
        long updated = jpaQueryFactory
                .update(chatRoomMember)
                .set(chatRoomMember.unreadCount, chatRoomMember.unreadCount.add(delta))
                .where(
                        chatRoomMember.roomId.eq(roomId),
                        chatRoomMember.userId.eq(userId),
                        chatRoomMember.active.isTrue()
                )
                .execute();

        return (int) updated;
    }

    @Override
    public int resetUnreadCount(long roomId, long userId) {
        long updated = jpaQueryFactory
                .update(chatRoomMember)
                .set(chatRoomMember.unreadCount, 0L)
                .where(
                        chatRoomMember.roomId.eq(roomId),
                        chatRoomMember.userId.eq(userId),
                        chatRoomMember.active.isTrue()
                )
                .execute();

        return (int) updated;
    }

    @Override
    public int updateLastReadMessageIdMax(long roomId, long userId, long next) {
        NumberExpression<Long> nextExpr = Expressions.asNumber(next);

        NumberExpression<Long> coalesceExpr =
                Expressions.numberTemplate(Long.class, "COALESCE({0}, {1})", chatRoomMember.lastReadMessageId, 0L);

        NumberExpression<Long> greatestExpr =
                Expressions.numberTemplate(Long.class, "GREATEST({0}, {1})", coalesceExpr, nextExpr);

        long updated = jpaQueryFactory
                .update(chatRoomMember)
                .set(chatRoomMember.lastReadMessageId, greatestExpr)
                .where(
                        chatRoomMember.roomId.eq(roomId),
                        chatRoomMember.userId.eq(userId),
                        chatRoomMember.active.isTrue()
                )
                .execute();

        return (int) updated;
    }
}
