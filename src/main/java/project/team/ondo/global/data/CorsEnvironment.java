package project.team.ondo.global.data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@ConfigurationProperties(prefix = "spring.security.cors")
@Validated
public record CorsEnvironment(
    List<String> allowedOrigins
) {
}
