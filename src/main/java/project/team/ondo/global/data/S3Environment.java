package project.team.ondo.global.data;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws.s3")
public record S3Environment(
    String bucket,
    String region,
    Presign presign
) {
    public record Presign(
            long putExpirationSeconds,
            long getExpirationSeconds
    ) {}
}
