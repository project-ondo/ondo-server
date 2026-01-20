package project.team.ondo.domain.chat.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.team.ondo.domain.chat.entity.QChatRoomEntity;
import project.team.ondo.domain.chat.entity.QChatRoomMemberEntity;
import project.team.ondo.domain.chat.repository.ChatRoomMuteRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomMuteRepositoryImpl implements ChatRoomMuteRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private final QChatRoomEntity chatRoom = QChatRoomEntity.chatRoomEntity;
    private final QChatRoomMemberEntity chatRoomMember = QChatRoomMemberEntity.chatRoomMemberEntity;

    @Override
    public boolean isMuted(UUID chatRoomPublicId, long userId) {
        Boolean muted = jpaQueryFactory
                .select(chatRoomMember.muted)
                .from(chatRoomMember)
                .join(chatRoom).on(chatRoom.publicId.eq(chatRoomPublicId))
                .where(
                        chatRoom.publicId.eq(chatRoomPublicId),
                        chatRoomMember.userId.eq(userId),
                        chatRoomMember.active.isTrue()
                )
                .fetchOne();

        return Boolean.TRUE.equals(muted);
    }

    @Override
    public void mute(UUID chatRoomPublicId, long userId) {
        jpaQueryFactory
                .update(chatRoomMember)
                .set(chatRoomMember.muted, true)
                .where(
                        chatRoomMember.userId.eq(userId),
                        chatRoomMember.active.isTrue(),
                        chatRoomMember.roomId.eq(
                                jpaQueryFactory.select(chatRoom.id)
                                        .from(chatRoom)
                                        .where(chatRoom.publicId.eq(chatRoomPublicId))
                        )
                )
                .execute();
    }

    @Override
    public void unmute(UUID chatRoomPublicId, long userId) {
        jpaQueryFactory
                .update(chatRoomMember)
                .set(chatRoomMember.muted, false)
                .where(
                        chatRoomMember.userId.eq(userId),
                        chatRoomMember.active.isTrue(),
                        chatRoomMember.roomId.eq(
                                jpaQueryFactory.select(chatRoom.id)
                                        .from(chatRoom)
                                        .where(chatRoom.publicId.eq(chatRoomPublicId))
                        )
                )
                .execute();
    }
}
