package project.team.ondo.global.data;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oci.s3")
public record S3Environment(
    String bucket,
    String region,
    String endpoint,
    Presign presign
) {
    public record Presign(
            long putExpirationSeconds,
            long getExpirationSeconds
    ) {}
}
