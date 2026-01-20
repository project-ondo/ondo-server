package project.team.ondo.domain.chat.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import project.team.ondo.domain.chat.data.request.EnterRoomRequest;
import project.team.ondo.domain.chat.service.ChatPresenceService;

import java.security.Principal;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChatRoomPresenceController {

    private final ChatPresenceService chatPresenceService;

    @MessageMapping("/chat.room.enter")
    public void enter(@Valid @Payload EnterRoomRequest request, Principal principal) {
        chatPresenceService.enterRoom(UUID.fromString(principal.getName()), request.roomId());
    }

    @MessageMapping("/chat.room.leave")
    public void leave(Principal principal) {
        chatPresenceService.leaveRoom(UUID.fromString(principal.getName()));
    }

    @MessageMapping("/chat.ping")
    public void ping(Principal principal) {
        chatPresenceService.markOnline(UUID.fromString(principal.getName()));
    }
}
