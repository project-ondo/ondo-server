package project.team.ondo.global.security.jwt.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class UserRefreshTokenIndexRepository {

    private static final String KEY_PREFIX = "user_refresh_tokens:";

    private final StringRedisTemplate stringRedisTemplate;

    public void add(String userId, String refreshToken, long ttlSeconds) {
        long expireAt = System.currentTimeMillis() + (ttlSeconds * 1000);
        stringRedisTemplate.opsForZSet().add(key(userId), refreshToken, expireAt);
    }

    public void remove(String userId, String refreshToken) {
        stringRedisTemplate.opsForZSet().remove(key(userId), refreshToken);
    }

    public Set<String> findAll(String userId) {
        String key = key(userId);
        long now = System.currentTimeMillis();
        stringRedisTemplate.opsForZSet().removeRangeByScore(key, 0, now);
        Set<String> tokens = stringRedisTemplate.opsForZSet().range(key, 0, -1);
        return tokens != null ? tokens : Collections.emptySet();
    }

    public void deleteAll(String userId) {
        stringRedisTemplate.delete(key(userId));
    }

    private String key(String userId) {
        return KEY_PREFIX + userId;
    }
}
