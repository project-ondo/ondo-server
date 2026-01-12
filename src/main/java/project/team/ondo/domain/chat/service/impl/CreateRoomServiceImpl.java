package project.team.ondo.domain.chat.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.chat.entity.ChatRoomEntity;
import project.team.ondo.domain.chat.entity.ChatRoomMemberEntity;
import project.team.ondo.domain.chat.exception.ChatRoomNotFoundException;
import project.team.ondo.domain.chat.repository.ChatRoomMemberRepository;
import project.team.ondo.domain.chat.repository.ChatRoomRepository;
import project.team.ondo.domain.chat.service.CreateRoomService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.exception.UserNotFoundException;
import project.team.ondo.domain.user.repository.UserRepository;
import project.team.ondo.global.security.jwt.service.CurrentUserProvider;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateRoomServiceImpl implements CreateRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final UserRepository userRepository;
    private final CurrentUserProvider currentUserProvider;

    @Override
    @Transactional
    public UUID execute(UUID targetUserPublicId) {
        UserEntity me = currentUserProvider.getCurrentUser();
        UserEntity targetUser = userRepository.findByPublicId(targetUserPublicId)
                .orElseThrow(UserNotFoundException::new);

        long a = Math.min(me.getId(), targetUser.getId());
        long b = Math.max(me.getId(), targetUser.getId());

        ChatRoomEntity chatRoom;
        try {
            chatRoom = chatRoomRepository.findByUserAIdAndUserBId(a, b)
                    .orElseGet(() -> chatRoomRepository.save(ChatRoomEntity.create(a, b)));
        } catch (DataIntegrityViolationException e) {
            chatRoom = chatRoomRepository.findByUserAIdAndUserBId(a, b).orElseThrow(ChatRoomNotFoundException::new);
        }

        final long roomId = chatRoom.getId();
        final long meId = me.getId();
        final long targetUserId = targetUser.getId();

        ChatRoomMemberEntity meMember = chatRoomMemberRepository.findByRoomIdAndUserId(roomId, meId).orElse(null);

        if (meMember == null) {
            chatRoomMemberRepository.save(ChatRoomMemberEntity.create(roomId, meId));
        } else if (!meMember.isActive()) {
            meMember.join();
        }

        ChatRoomMemberEntity targetMember = chatRoomMemberRepository.findByRoomIdAndUserId(roomId, targetUserId).orElse(null);
        if (targetMember == null) {
            chatRoomMemberRepository.save(ChatRoomMemberEntity.create(roomId, targetUserId));
        } else if (!targetMember.isActive()) {
            targetMember.join();
        }

        return chatRoom.getPublicId();
    }
}
