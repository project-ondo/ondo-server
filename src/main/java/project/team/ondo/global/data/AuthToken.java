package project.team.ondo.global.data;

import java.time.LocalDateTime;

public record AuthToken(
        String token,
        LocalDateTime expiration
) {
}
