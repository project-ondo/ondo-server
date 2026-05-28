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

    public void add(String userId, String refreshToken) {
        stringRedisTemplate.opsForSet().add(key(userId), refreshToken);
    }

    public void remove(String userId, String refreshToken) {
        stringRedisTemplate.opsForSet().remove(key(userId), refreshToken);
    }

    public Set<String> findAll(String userId) {
        Set<String> tokens = stringRedisTemplate.opsForSet().members(key(userId));
        return tokens != null ? tokens : Collections.emptySet();
    }

    public void deleteAll(String userId) {
        stringRedisTemplate.delete(key(userId));
    }

    private String key(String userId) {
        return KEY_PREFIX + userId;
    }
}
