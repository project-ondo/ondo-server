package project.team.ondo.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project.team.ondo.global.data.S3Environment;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {

    @Bean
    public S3Presigner s3Presigner(S3Environment s3Environment) {
        return S3Presigner.builder()
                .region(Region.of(s3Environment.region()))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}
