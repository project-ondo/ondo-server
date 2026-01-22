package project.team.ondo.global.data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "spring.security.jwt")
@Validated
public record JwtEnvironment(
    AccessToken accessToken,
    RefreshToken refreshToken
) {
    public record AccessToken(
            String secret,
            long expiration
    ){}

    public record RefreshToken(
            String secret,
            long expiration
    ){}
}
