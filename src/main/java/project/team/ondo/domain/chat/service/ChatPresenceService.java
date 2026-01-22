package project.team.ondo.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatPresenceService {

    private static final Duration TTL = Duration.ofSeconds(90);

    private final StringRedisTemplate redisTemplate;

    private String onlineKey(UUID userPublicId) {
        return "ws:online:" + userPublicId;
    }

    private String roomKey(UUID userPublicId) {
        return "ws:room:" + userPublicId;
    }

    public void markOnline(UUID userPublicId) {
        redisTemplate.opsForValue().set(onlineKey(userPublicId), "1", TTL);
    }

    public void markOffline(UUID userPublicId) {
        redisTemplate.delete(onlineKey(userPublicId));
        redisTemplate.delete(roomKey(userPublicId));
    }

    public void enterRoom(UUID userPublicId, UUID roomId) {
        redisTemplate.opsForValue().set(onlineKey(userPublicId), "1", TTL);
        redisTemplate.opsForValue().set(roomKey(userPublicId), roomId.toString(), TTL);
    }

    public void leaveRoom(UUID userPublicId) {
        redisTemplate.opsForValue().set(onlineKey(userPublicId), "1", TTL);
        redisTemplate.delete(roomKey(userPublicId));
    }

    public boolean isOnline(UUID userPublicId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(onlineKey(userPublicId)));
    }

    public Optional<UUID> currentRoomId(UUID userPublicId) {
        String v = redisTemplate.opsForValue().get(roomKey(userPublicId));
        if (v == null || v.isBlank()) return Optional.empty();
        try {
            return Optional.of(UUID.fromString(v));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public boolean isViewingRoom(UUID userPublicId, UUID roomId) {
        if (!isOnline(userPublicId)) return false;
        return currentRoomId(userPublicId)
                .map(id -> id.equals(roomId))
                .orElse(false);
    }
}
