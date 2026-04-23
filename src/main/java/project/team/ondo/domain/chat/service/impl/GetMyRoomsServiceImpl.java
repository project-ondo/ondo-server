package project.team.ondo.domain.chat.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.chat.data.response.ChatRoomListItemResponse;
import project.team.ondo.domain.chat.repository.ChatRoomRepository;
import project.team.ondo.domain.chat.service.ChatPresenceService;
import project.team.ondo.domain.chat.service.GetMyRoomsService;
import project.team.ondo.domain.user.entity.UserEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetMyRoomsServiceImpl implements GetMyRoomsService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatPresenceService chatPresenceService;

    @Transactional(readOnly = true)
    @Override
    public Page<@NonNull ChatRoomListItemResponse> execute(UserEntity me, Pageable pageable) {

        Page<@NonNull ChatRoomListItemResponse> page =
                chatRoomRepository.findMyRooms(me.getId(), pageable);

        List<UUID> opponentIds = page.getContent().stream()
                .map(ChatRoomListItemResponse::opponentPublicId)
                .toList();

        Map<UUID, Boolean> onlineMap = chatPresenceService.batchIsOnline(opponentIds);

        List<ChatRoomListItemResponse> patched = page.getContent().stream()
                .map(r -> new ChatRoomListItemResponse(
                        r.roomId(),
                        r.opponentPublicId(),
                        r.opponentDisplayName(),
                        r.opponentProfileImageKey(),
                        Boolean.TRUE.equals(onlineMap.get(r.opponentPublicId())),
                        r.unreadCount(),
                        r.lastMessagePreview(),
                        false,
                        r.lastMessageAt()
                ))
                .toList();

        return new PageImpl<>(patched, pageable, page.getTotalElements());
    }
}
