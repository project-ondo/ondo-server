package project.team.ondo.domain.chat.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import project.team.ondo.domain.chat.data.request.SendMessageRequest;
import project.team.ondo.domain.chat.data.response.ChatMessageResponse;
import project.team.ondo.domain.chat.entity.ChatMessageEntity;
import project.team.ondo.domain.chat.service.SendMessageService;

import java.security.Principal;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SendMessageService sendMessageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat.send")
    public void send(@Valid @Payload SendMessageRequest request, Principal principal) {

        UUID senderPublicId = UUID.fromString(principal.getName());

        ChatMessageEntity savedMessage = sendMessageService.execute(
                senderPublicId,
                request.roomId(),
                request.messageType(),
                request.content()
        );

        ChatMessageResponse payload = new ChatMessageResponse(
                savedMessage.getId(),
                request.roomId(),
                savedMessage.getSenderId(),
                savedMessage.getMessageType(),
                savedMessage.getContent(),
                savedMessage.getCreatedAt()
        );

        simpMessagingTemplate.convertAndSend("/topic/chat.rooms." + request.roomId(), payload);
    }
}
