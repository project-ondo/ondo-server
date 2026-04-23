package project.team.ondo.domain.chat.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import project.team.ondo.domain.chat.data.payload.ChatPresencePayload;
import project.team.ondo.domain.chat.data.request.EnterRoomRequest;
import project.team.ondo.domain.chat.data.request.LeaveRoomRequest;
import project.team.ondo.domain.chat.service.ChatPresenceService;
import project.team.ondo.domain.chat.service.ChatRoomMembershipService;
import project.team.ondo.domain.chat.service.ChatWsPushService;
import project.team.ondo.domain.user.repository.UserRepository;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChatRoomPresenceController {


    private final ChatPresenceService chatPresenceService;
    private final ChatWsPushService chatwsPushService;
    private final ChatRoomMembershipService chatRoomMembershipService;
    private final UserRepository userRepository;

    @MessageMapping("/chat.room.enter")
    public void enter(@Valid @Payload EnterRoomRequest request, Principal principal) {
        UUID userPublicId = UUID.fromString(principal.getName());
        long userId = userRepository.getByPublicId(userPublicId).getId();
        chatRoomMembershipService.validateAndGet(request.chatRoomPublicId(), userId);

        chatPresenceService.enterRoom(userPublicId, request.chatRoomPublicId());

        chatwsPushService.broadcastPresence(
                request.chatRoomPublicId(),
                new ChatPresencePayload(userPublicId, true, request.chatRoomPublicId(), LocalDateTime.now())
        );
    }

    @MessageMapping("/chat.room.leave")
    public void leave(@Valid @Payload LeaveRoomRequest request, Principal principal) {
        UUID userPublicId = UUID.fromString(principal.getName());
        long userId = userRepository.getByPublicId(userPublicId).getId();
        chatRoomMembershipService.validateAndGet(request.chatRoomPublicId(), userId);

        chatPresenceService.leaveRoom(userPublicId);

        chatwsPushService.broadcastPresence(
                request.chatRoomPublicId(),
                new ChatPresencePayload(userPublicId, false, null, LocalDateTime.now())
        );
    }
}
