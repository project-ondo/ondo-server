package project.team.ondo.domain.auth.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@RedisHash(value = "auth_code")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthCodeEntity {

    @Id
    private String email;
    private String code;
    private Integer attemptCount;
    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long ttl;

    public void increaseAttemptCount() {
        this.attemptCount = (this.attemptCount == null ? 1 : this.attemptCount + 1);
    }
}
