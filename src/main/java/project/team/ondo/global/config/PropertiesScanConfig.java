package project.team.ondo.global.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import project.team.ondo.global.data.CorsEnvironment;
import project.team.ondo.global.data.JwtEnvironment;
import project.team.ondo.global.data.EmailEnvironment;
import project.team.ondo.global.data.RabbitmqEnvironment;

@EnableConfigurationProperties({
        RabbitmqEnvironment.class,
        JwtEnvironment.class,
        CorsEnvironment.class,
        EmailEnvironment.class
})
@Configuration
public class PropertiesScanConfig {
}
