package project.team.ondo.global.stomp.interceptor;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import project.team.ondo.domain.user.constant.UserRole;
import project.team.ondo.global.security.jwt.service.JwtParserService;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StompJwtAuthChannelInterceptor implements ChannelInterceptor {

    private final JwtParserService jwtParserService;

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null || accessor.getCommand() == null) {
            return message;
        }

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new MessagingException("Missing Authorization header");
            }

            String token = authHeader.substring(7);
            if (!Boolean.TRUE.equals(jwtParserService.validateAccessToken(token))) {
                throw new MessagingException("Invalid access token");
            }

            UUID publicId = UUID.fromString(jwtParserService.getUserIdFromAccessToken(token));
            UserRole role = jwtParserService.getRoleFromAccessToken(token);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    publicId.toString(),
                    null,
                    List.of(new SimpleGrantedAuthority(role.name()))
            );

            accessor.setUser(authentication);

            return message;
        }

        if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            SecurityContextHolder.clearContext();
            return message;
        }

        Object user = accessor.getUser();
        if (user instanceof Authentication authentication) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        return message;
    }
}
