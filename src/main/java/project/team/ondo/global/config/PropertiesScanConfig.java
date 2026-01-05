package project.team.ondo.global.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import project.team.ondo.global.rabbitmq.data.RabbitmqEnvironment;

@EnableConfigurationProperties(
        RabbitmqEnvironment.class
)
@Configuration
public class PropertiesScanConfig {
}
