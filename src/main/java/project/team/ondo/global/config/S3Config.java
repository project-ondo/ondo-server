package project.team.ondo.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project.team.ondo.global.data.S3Environment;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
public class S3Config {

    @Bean
    public S3Presigner s3Presigner(S3Environment s3Environment) {

        String accessKey = System.getenv("OCI_ACCESS_KEY");
        String secretKey = System.getenv("OCI_SECRET_KEY");

        if (accessKey == null || secretKey == null) {
            throw new IllegalStateException("OCI_ACCESS_KEY and OCI_SECRET_KEY environment variables must be set");
        }

        return S3Presigner.builder()
                .region(Region.of(s3Environment.region()))
                .endpointOverride(URI.create(s3Environment.endpoint()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)
                        )
                )
                .serviceConfiguration(
                        S3Configuration.builder()
                                .pathStyleAccessEnabled(true)
                                .build()
                )
                .build();
    }
}
