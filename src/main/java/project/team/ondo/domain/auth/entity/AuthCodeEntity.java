package project.team.ondo.domain.auth.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.concurrent.TimeUnit;

@RedisHash(value = "auth_code")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthCodeEntity {

    @Id
    private String email;
    @Indexed
    private String code;
    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long ttl;
}
