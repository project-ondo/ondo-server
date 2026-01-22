package project.team.ondo.global.stomp.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import project.team.ondo.domain.chat.service.ChatPresenceService;
import project.team.ondo.global.stomp.extractor.StompAuthExtractor;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StompPresenceEventHandler {

    private final ChatPresenceService chatPresenceService;

    @EventListener
    public void handleConnect(SessionConnectEvent event) {
        UUID userPublicId = StompAuthExtractor.extractUserPublicId(event.getMessage());
        if (userPublicId != null) chatPresenceService.markOnline(userPublicId);
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        UUID userPublicId = StompAuthExtractor.extractUserPublicId(event.getMessage());
        if (userPublicId != null) {
            chatPresenceService.markOffline(userPublicId);
        }
    }
}
