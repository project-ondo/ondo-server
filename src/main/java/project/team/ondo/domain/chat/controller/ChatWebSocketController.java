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

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SendMessageService sendMessageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat.send")
    public void send(@Valid @Payload SendMessageRequest request) {
        ChatMessageEntity savedMessage = sendMessageService.execute(
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

        simpMessagingTemplate.convertAndSend("/sub/chat/rooms" + request.roomId(), payload);
    }
}
