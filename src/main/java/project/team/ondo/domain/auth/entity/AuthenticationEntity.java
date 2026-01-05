package project.team.ondo.domain.auth.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@RedisHash(value = "authentication")
@Getter
@Builder
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
public class AuthenticationEntity {

    @Id
    private String email;
    private Integer attemptCount;
    private Boolean verified;
    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long ttl;
}
