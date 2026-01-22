package project.team.ondo.global.stomp.extractor;

import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import java.security.Principal;
import java.util.UUID;

public final class StompAuthExtractor {

    private StompAuthExtractor() {}

    public static UUID extractUserPublicId(Message<?> message) {
        StompHeaderAccessor stomp = StompHeaderAccessor.wrap(message);
        UUID fromStomp = extractFromPrincipal(stomp.getUser());
        if (fromStomp != null) return fromStomp;

        SimpMessageHeaderAccessor simp = SimpMessageHeaderAccessor.wrap(message);
        return extractFromPrincipal(simp.getUser());
    }

    private static UUID extractFromPrincipal(Principal principal) {
        if (principal == null) return null;

        String name = principal.getName();
        if (name == null || name.isBlank()) return null;

        try {
            return UUID.fromString(name);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
