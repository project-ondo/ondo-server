package project.team.ondo.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import project.team.ondo.domain.chat.data.payload.ChatPresencePayload;
import project.team.ondo.domain.chat.data.payload.ChatRoomListUpdatePayload;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatWsPushService {

    private final SimpMessagingTemplate template;

    public void pushRoomListUpdateToUser(UUID userPublicId, ChatRoomListUpdatePayload payload) {
        template.convertAndSendToUser(
                userPublicId.toString(),
                "/queue/chat.rooms.update",
                payload
        );
    }

    public void broadcastPresence(UUID roomPublicId, ChatPresencePayload payload) {
        template.convertAndSend("/topic/chat.rooms." + roomPublicId + ".presence", payload);
    }
}
