package project.team.ondo.global.data;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.mail")
public record MailEnvironment(
    String host,
    int port,
    String username,
    String password
) {
}
