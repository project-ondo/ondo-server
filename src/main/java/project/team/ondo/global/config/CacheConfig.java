package project.team.ondo.global.config;

import lombok.NonNull;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import project.team.ondo.domain.user.cache.RecommendUserCache;
import project.team.ondo.domain.user.data.UserRecommendCacheValue;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory, ObjectMapper objectMapper) {

        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));

        JacksonJsonRedisSerializer<@NonNull UserRecommendCacheValue> recommendSerializer =
                new JacksonJsonRedisSerializer<>(objectMapper, UserRecommendCacheValue.class);

        RedisCacheConfiguration recommendConfig = defaultConfig
                .entryTtl(Duration.ofMinutes(15))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(recommendSerializer)
                );

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultConfig)
                .withCacheConfiguration(RecommendUserCache.CACHE_NAME, recommendConfig)
                .build();
    }
}