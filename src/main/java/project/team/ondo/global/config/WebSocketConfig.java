package project.team.ondo.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import project.team.ondo.global.data.RabbitmqEnvironment;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final RabbitmqEnvironment rabbitmqEnvironment;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*");
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/pub");
        registry.setUserDestinationPrefix("/user");

        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        scheduler.setThreadNamePrefix("wss-heartbeat-");
        scheduler.initialize();

        registry.enableStompBrokerRelay("/topic", "/queue")
                .setRelayHost(rabbitmqEnvironment.host())
                .setRelayPort(rabbitmqEnvironment.port())
                .setClientLogin(rabbitmqEnvironment.username())
                .setClientPasscode(rabbitmqEnvironment.password())
                .setSystemLogin(rabbitmqEnvironment.host())
                .setSystemPasscode(rabbitmqEnvironment.password())
                .setSystemHeartbeatSendInterval(10000)
                .setSystemHeartbeatReceiveInterval(10000)
                .setTaskScheduler(scheduler);
    }
}
