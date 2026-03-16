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

        List<ChatRoomListItemResponse> patched = page.getContent().stream()
                .map(r -> new ChatRoomListItemResponse(
                        r.roomId(),
                        r.opponentPublicId(),
                        r.opponentDisplayName(),
                        r.opponentProfileImageKey(),
                        chatPresenceService.isOnline(r.opponentPublicId()),
                        r.unreadCount(),
                        r.lastMessagePreview(),
                        false,
                        r.lastMessageAt()
                ))
                .toList();

        return new PageImpl<>(patched, pageable, page.getTotalElements());
    }
}
