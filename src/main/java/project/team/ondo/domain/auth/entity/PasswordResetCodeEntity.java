package project.team.ondo.domain.auth.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@RedisHash(value = "password_reset_code")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PasswordResetCodeEntity {

    @Id
    private String email;
    private String code;
    @Builder.Default
    private int attemptCount = 0;
    @TimeToLive(unit = TimeUnit.SECONDS)
    private Long ttl;

    public void increaseAttemptCount() {
        this.attemptCount++;
    }
}