package project.team.ondo.global.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import project.team.ondo.global.data.*;

@EnableConfigurationProperties({
        RabbitmqEnvironment.class,
        JwtEnvironment.class,
        CorsEnvironment.class,
        EmailEnvironment.class,
        S3Environment.class
})
@Configuration
public class PropertiesScanConfig {
}
