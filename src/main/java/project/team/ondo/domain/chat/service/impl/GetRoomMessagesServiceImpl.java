package project.team.ondo.domain.chat.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.chat.data.response.ChatMessageResponse;
import project.team.ondo.domain.chat.entity.ChatMessageEntity;
import project.team.ondo.domain.chat.entity.ChatRoomEntity;
import project.team.ondo.domain.chat.exception.ChatRoomMemberNotFoundException;
import project.team.ondo.domain.chat.exception.ChatRoomNotFoundException;
import project.team.ondo.domain.chat.repository.ChatMessageRepository;
import project.team.ondo.domain.chat.repository.ChatRoomMemberRepository;
import project.team.ondo.domain.chat.repository.ChatRoomRepository;
import project.team.ondo.domain.chat.service.GetRoomMessagesService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.global.response.CursorResponse;
import project.team.ondo.global.security.jwt.service.CurrentUserProvider;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetRoomMessagesServiceImpl implements GetRoomMessagesService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final CurrentUserProvider currentUserProvider;

    @Transactional(readOnly = true)
    @Override
    public CursorResponse<@NonNull ChatMessageResponse> execute(UUID roomPublicId, Long cursor, int size) {
        UserEntity me = currentUserProvider.getCurrentUser();

        ChatRoomEntity chatRoom = chatRoomRepository.findByPublicId(roomPublicId)
                .orElseThrow(ChatRoomNotFoundException::new);

        chatRoomMemberRepository.findByRoomIdAndUserId(chatRoom.getId(), me.getId())
                .orElseThrow(ChatRoomMemberNotFoundException::new);

        Pageable pageable = PageRequest.of(0, size);

        Page<@NonNull ChatMessageEntity> page = (cursor == null)
                ? chatMessageRepository.findAllByRoomIdOrderByIdDesc(chatRoom.getId(), pageable)
                : chatMessageRepository.findAllByRoomIdAndIdLessThanOrderByIdDesc(chatRoom.getId(), cursor, pageable);

        List<ChatMessageResponse> items = page.getContent().stream()
                .map(message -> new ChatMessageResponse(
                        message.getId(),
                        roomPublicId,
                        message.getSenderId(),
                        message.getMessageType(),
                        message.getContent(),
                        message.getCreatedAt()
                ))
                .toList();

        Long nextCursor = items.isEmpty() ? null : items.getLast().messageId();

        return CursorResponse.of(items, nextCursor, page.hasNext());
    }
}
