package project.team.ondo.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatTypingThrottleService {

    private static final Duration TTL = Duration.ofSeconds(1);

    private final StringRedisTemplate redisTemplate;

    private String key(UUID chatRoomPublicId, UUID userPublicId) {
        return "ws:typing" + chatRoomPublicId + ":" + userPublicId;
    }

    public boolean allow(UUID chatRoomPublicId, UUID userPublicId) {
        String k = key(chatRoomPublicId, userPublicId);
        Boolean ok = redisTemplate.opsForValue().setIfAbsent(k, "1", TTL);
        return Boolean.TRUE.equals(ok);
    }
}
