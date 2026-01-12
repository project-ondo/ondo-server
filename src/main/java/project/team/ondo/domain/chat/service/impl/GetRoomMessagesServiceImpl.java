package project.team.ondo.domain.chat.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
import project.team.ondo.global.security.jwt.service.CurrentUserProvider;

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
    public Page<@NonNull ChatMessageResponse> execute(UUID roomId, Pageable pageable) {
        UserEntity me = currentUserProvider.getCurrentUser();
        ChatRoomEntity chatRoom = chatRoomRepository.findByPublicId(roomId).orElseThrow(ChatRoomNotFoundException::new);

        chatRoomMemberRepository.findByRoomIdAndUserId(chatRoom.getId(), me.getId()).orElseThrow(ChatRoomMemberNotFoundException::new);

        Page<@NonNull ChatMessageEntity> page = chatMessageRepository.findAllByRoomIdOrderByIdDesc(chatRoom.getId(), pageable);

        return page.map(message -> new ChatMessageResponse(
                message.getId(),
                roomId,
                message.getSenderId(),
                message.getMessageType(),
                message.getContent(),
                message.getCreatedAt()
        ));
    }
}
