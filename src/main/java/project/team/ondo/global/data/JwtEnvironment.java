package project.team.ondo.global.data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@ConfigurationProperties(prefix = "spring.security.jwt")
@Validated
public record JwtEnvironment(
    AccessToken accessToken,
    RefreshToken refreshToken
) {
    public record AccessToken(
            String secret,
            LocalDateTime expiration
    ){}

    public record RefreshToken(
            String secret,
            LocalDateTime expiration
    ){}
}
