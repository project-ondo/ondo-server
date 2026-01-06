package project.team.ondo.global.data;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "messaging.relay")
public record RabbitmqEnvironment(
    String host,
    int port,
    String username,
    String password
) {
}
