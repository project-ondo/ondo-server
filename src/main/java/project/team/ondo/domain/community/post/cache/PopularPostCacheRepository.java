package project.team.ondo.domain.community.post.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PopularPostCacheRepository {

    static final String KEY = "popular:posts";
    private static final Duration TTL = Duration.ofHours(2);

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    public void save(List<PopularPostCacheItem> items) {
        try {
            String json = objectMapper.writeValueAsString(items);
            stringRedisTemplate.opsForValue().set(KEY, json, TTL);
        } catch (Exception e) {
            throw new IllegalStateException("인기 게시물 캐시 저장 실패", e);
        }
    }

    public List<PopularPostCacheItem> findAll() {
        String json = stringRedisTemplate.opsForValue().get(KEY);
        if (json == null) {
            return List.of();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<PopularPostCacheItem>>() {});
        } catch (Exception e) {
            return List.of();
        }
    }
}
