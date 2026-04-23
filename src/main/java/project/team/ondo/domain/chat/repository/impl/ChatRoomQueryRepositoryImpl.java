package project.team.ondo.domain.chat.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.team.ondo.domain.chat.data.response.ChatRoomListItemResponse;
import project.team.ondo.domain.chat.entity.QChatMessageEntity;
import project.team.ondo.domain.chat.entity.QChatRoomEntity;
import project.team.ondo.domain.chat.entity.QChatRoomMemberEntity;
import project.team.ondo.domain.chat.repository.ChatRoomQueryRepository;
import project.team.ondo.domain.chat.constant.ChatConstants;
import project.team.ondo.domain.user.entity.QUserEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomQueryRepositoryImpl implements ChatRoomQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private final QChatRoomEntity chatRoom = QChatRoomEntity.chatRoomEntity;
    private final QChatRoomMemberEntity chatRoomMember = QChatRoomMemberEntity.chatRoomMemberEntity;
    private final QChatMessageEntity chatMessage = QChatMessageEntity.chatMessageEntity;
    private final QUserEntity user = QUserEntity.userEntity;

    @Override
    public Page<@NonNull ChatRoomListItemResponse> findMyRooms(long meId, Pageable pageable) {

        NumberExpression<Long> opponentIdExpr =
                new CaseBuilder()
                        .when(chatRoom.userAId.eq(meId)).then(chatRoom.userBId)
                        .otherwise(chatRoom.userAId);

        QChatMessageEntity m2 = new QChatMessageEntity("m2");
        var lastMessageIdSubQuery = JPAExpressions
                .select(m2.id.max())
                .from(m2)
                .where(m2.roomId.eq(chatRoom.id));

        List<Tuple> rows = jpaQueryFactory
                .select(
                        chatRoom.publicId,
                        user.publicId,
                        user.displayName,
                        user.profileImageKey,
                        chatRoomMember.muted,
                        chatRoomMember.unreadCount,
                        chatMessage.content,
                        chatMessage.createdAt
                )
                .from(chatRoomMember)
                .join(chatRoom).on(chatRoom.id.eq(chatRoomMember.roomId))
                .join(user).on(user.id.eq(opponentIdExpr))
                .leftJoin(chatMessage).on(chatMessage.id.eq(lastMessageIdSubQuery))
                .where(
                        chatRoomMember.userId.eq(meId),
                        chatRoomMember.active.isTrue(),
                        chatRoomMember.blocked.isFalse()
                )
                .orderBy(
                        chatMessage.id.desc().nullsLast(),
                        chatRoom.id.desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(chatRoomMember.count())
                .from(chatRoomMember)
                .where(
                        chatRoomMember.userId.eq(meId),
                        chatRoomMember.active.isTrue(),
                        chatRoomMember.blocked.isFalse()
                )
                .fetchOne();

        long totalCount = total == null ? 0L : total;

        List<ChatRoomListItemResponse> content = rows.stream()
                .map(t -> {
                    UUID roomPublicId = t.get(chatRoom.publicId);
                    UUID opponentPublicId = t.get(user.publicId);
                    String opponentDisplayName = t.get(user.displayName);
                    String opponentProfileImage = t.get(user.profileImageKey);
                    Boolean muted = t.get(chatRoomMember.muted);
                    Long unread = t.get(chatRoomMember.unreadCount);
                    String lastContent = t.get(chatMessage.content);
                    LocalDateTime lastAt = t.get(chatMessage.createdAt);

                    String preview = (lastContent == null) ? "" : lastContent;
                    if (preview.length() > ChatConstants.PREVIEW_MAX_LENGTH) preview = preview.substring(0, ChatConstants.PREVIEW_MAX_LENGTH);

                    return new ChatRoomListItemResponse(
                            roomPublicId,
                            opponentPublicId,
                            opponentDisplayName,
                            opponentProfileImage,
                            false,
                            unread == null ? 0L : unread,
                            preview,
                            false,
                            lastAt
                    );
                })
                .toList();

        return new PageImpl<>(content, pageable, totalCount);
    }
}
