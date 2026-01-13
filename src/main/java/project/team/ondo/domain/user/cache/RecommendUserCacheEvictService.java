package project.team.ondo.domain.user.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecommendUserCacheEvictService {

    private final StringRedisTemplate stringRedisTemplate;

    public void evict(UUID mePublicId) {
        String pattern = RecommendUserCache.CACHE_NAME + "::" + RecommendUserCache.KEY_PREFIX + mePublicId + ":*";

        Set<String> keys = stringRedisTemplate.keys(pattern);
        if (keys == null || keys.isEmpty()) {
            return;
        }

        stringRedisTemplate.delete(keys);
    }
}
