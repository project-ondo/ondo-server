package project.team.ondo.global.lock;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class DistributedLockService {

    private static final int MAX_RETRY = 3;
    private static final long RETRY_INTERVAL_MS = 100L;

    private final StringRedisTemplate redisTemplate;

    public <T> T executeWithLock(String key, Duration ttl, Supplier<T> task) {
        for (int i = 0; i < MAX_RETRY; i++) {
            Boolean acquired = redisTemplate.opsForValue().setIfAbsent(key, "1", ttl);
            if (Boolean.TRUE.equals(acquired)) {
                try {
                    return task.get();
                } finally {
                    redisTemplate.delete(key);
                }
            }
            if (i < MAX_RETRY - 1) {
                try {
                    Thread.sleep(RETRY_INTERVAL_MS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted while waiting for lock: " + key, e);
                }
            }
        }
        throw new RuntimeException("Failed to acquire distributed lock after " + MAX_RETRY + " attempts: " + key);
    }
}
