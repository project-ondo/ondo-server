package project.team.ondo.domain.chat.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import project.team.ondo.domain.chat.data.payload.ChatReadEventPayload;
import project.team.ondo.domain.chat.data.payload.ChatTypingEventPayload;
import project.team.ondo.domain.chat.data.request.MarkReadWsRequest;
import project.team.ondo.domain.chat.data.request.SendMessageRequest;
import project.team.ondo.domain.chat.data.request.TypingWsRequest;
import project.team.ondo.domain.chat.data.response.ChatMessageResponse;
import project.team.ondo.domain.chat.entity.ChatMessageEntity;
import project.team.ondo.domain.chat.service.ChatPresenceService;
import project.team.ondo.domain.chat.service.ChatTypingThrottleService;
import project.team.ondo.domain.chat.service.MarkRoomReadService;
import project.team.ondo.domain.chat.service.SendMessageService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SendMessageService sendMessageService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatTypingThrottleService chatTypingThrottleService;
    private final MarkRoomReadService markRoomReadService;
    private final ChatPresenceService chatPresenceService;

    @MessageMapping("/chat.send")
    public void send(@Valid @Payload SendMessageRequest request, Principal principal) {

        UUID senderPublicId = UUID.fromString(principal.getName());

        ChatMessageEntity savedMessage = sendMessageService.execute(
                senderPublicId,
                request.chatRoomPublicId(),
                request.messageType(),
                request.content()
        );

        ChatMessageResponse payload = new ChatMessageResponse(
                savedMessage.getId(),
                request.chatRoomPublicId(),
                savedMessage.getSenderId(),
                savedMessage.getMessageType(),
                savedMessage.getContent(),
                savedMessage.getCreatedAt()
        );

        simpMessagingTemplate.convertAndSend("/topic/chat.rooms." + request.chatRoomPublicId(), payload);
    }

    @MessageMapping("/chat.read")
    public void read(@Valid @Payload MarkReadWsRequest request, Principal principal) {
        UUID readerPublicId = UUID.fromString(principal.getName());

        markRoomReadService.execute(readerPublicId, request.chatRoomPublicId(), request.lastReadMessageId());

        ChatReadEventPayload payload = new ChatReadEventPayload(
                request.chatRoomPublicId(),
                readerPublicId,
                request.lastReadMessageId(),
                LocalDateTime.now()
        );

        simpMessagingTemplate.convertAndSend(
                "/topic/chat.rooms." + request.chatRoomPublicId() + ".read",
                payload
        );
    }

    @MessageMapping("/chat.typing")
    public void typing(@Valid @Payload TypingWsRequest request, Principal principal) {
        UUID userPublicId = UUID.fromString(principal.getName());

        if (!chatTypingThrottleService.allow(request.chatRoomPublicId(), userPublicId)) return;

        ChatTypingEventPayload payload = new ChatTypingEventPayload(
                request.chatRoomPublicId(),
                userPublicId,
                Boolean.TRUE.equals(request.typing()),
                LocalDateTime.now()
        );

        simpMessagingTemplate.convertAndSend(
                "/topic/chat.rooms." + request.chatRoomPublicId() + ".typing",
                payload
        );
    }

    @MessageMapping("/chat.ping")
    public void ping(Principal principal) {
        UUID userPublicId = UUID.fromString(principal.getName());
        chatPresenceService.markOnline(userPublicId);
    }
}
