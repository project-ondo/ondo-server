package project.team.ondo.domain.chat.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.chat.data.response.ChatRoomListItemResponse;
import project.team.ondo.domain.chat.entity.ChatMessageEntity;
import project.team.ondo.domain.chat.entity.ChatRoomEntity;
import project.team.ondo.domain.chat.entity.ChatRoomMemberEntity;
import project.team.ondo.domain.chat.repository.ChatMessageRepository;
import project.team.ondo.domain.chat.repository.ChatRoomMemberRepository;
import project.team.ondo.domain.chat.repository.ChatRoomRepository;
import project.team.ondo.domain.chat.service.ChatPresenceService;
import project.team.ondo.domain.chat.service.GetMyRoomsService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.repository.UserRepository;
import project.team.ondo.global.security.jwt.service.CurrentUserProvider;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetMyRoomsServiceImpl implements GetMyRoomsService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final CurrentUserProvider currentUserProvider;
    private final ChatPresenceService chatPresenceService;

    @Transactional(readOnly = true)
    @Override
    public Page<@NonNull ChatRoomListItemResponse> execute(Pageable pageable) {
        UserEntity me = currentUserProvider.getCurrentUser();

        List<ChatRoomMemberEntity> members = chatRoomMemberRepository.findAllByUserIdAndActiveTrue(me.getId());

        if (members.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        List<Long> roomIds = members.stream()
                .map(ChatRoomMemberEntity::getRoomId).toList();

        Map<Long, ChatRoomMemberEntity> myMemberMap = members.stream()
                .collect(Collectors.toMap(ChatRoomMemberEntity::getRoomId, Function.identity()));

        List<ChatRoomEntity> rooms = chatRoomRepository.findAllById(roomIds);

        Map<Long, ChatRoomEntity> roomMap = rooms.stream()
                .collect(Collectors.toMap(ChatRoomEntity::getId, Function.identity()));

        List<ChatMessageEntity> lastMessages = chatMessageRepository.findLastMessages(roomIds);
        Map<Long, ChatMessageEntity> lastMessageMap = lastMessages.stream()
                .collect(Collectors.toMap(ChatMessageEntity::getRoomId, Function.identity()));

        List<Long> opponentIds = roomIds.stream()
                .map(roomId -> {
                    ChatRoomEntity room = roomMap.get(roomId);
                    if (room == null) return null;
                    return room.getUserAId().equals(me.getId()) ? room.getUserBId() : room.getUserAId();
                })
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        Map<Long, UserEntity> opponentMap = userRepository.findAllById(opponentIds).stream()
                .collect(Collectors.toMap(UserEntity::getId, Function.identity()));

        List<Long> orderedRoomIds = roomIds.stream()
                .sorted((r1, r2) -> {
                    ChatMessageEntity m1 = lastMessageMap.get(r1);
                    ChatMessageEntity m2 = lastMessageMap.get(r2);
                    long id1 = m1 == null ? 0L : m1.getId();
                    long id2 = m2 == null ? 0L : m2.getId();
                    return Long.compare(id2, id1);
                })
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), orderedRoomIds.size());

        if (start >= end) {
            return new PageImpl<>(List.of(), pageable, orderedRoomIds.size());
        }

        List<Long> pageRoomIds = orderedRoomIds.subList(start, end);
        List<ChatRoomListItemResponse> content = new ArrayList<>(pageRoomIds.size());

        for (Long roomId : pageRoomIds) {
            ChatRoomEntity room = roomMap.get(roomId);
            if (room == null) continue;

            ChatRoomMemberEntity myMember = myMemberMap.get(roomId);
            ChatMessageEntity lastMessage = lastMessageMap.get(roomId);

            Long opponentId = room.getUserAId().equals(me.getId()) ? room.getUserBId() : room.getUserAId();
            UserEntity opponent = opponentMap.get(opponentId);
            if (opponent == null) continue;

            long unread = chatMessageRepository.countUnreadMessages(roomId, me.getId(), myMember == null ? null : myMember.getLastReadMessageId());

            String preview = lastMessage == null ? "" : lastMessage.getContent();
            if (preview.length() > 30) preview = preview.substring(0, 30);

            LocalDateTime lastMessageAt = lastMessage == null ? null : lastMessage.getCreatedAt();

            boolean opponentOnline = chatPresenceService.isOnline(opponent.getPublicId());
            boolean muted = myMember != null && myMember.isMuted();

            content.add(new ChatRoomListItemResponse(
                    room.getPublicId(),
                    opponent.getPublicId(),
                    opponent.getDisplayName(),
                    opponent.getProfileImageKey(),
                    opponentOnline,
                    unread,
                    preview,
                    muted,
                    lastMessageAt
            ));
        }

        return new PageImpl<>(content, pageable, orderedRoomIds.size());
    }
}
