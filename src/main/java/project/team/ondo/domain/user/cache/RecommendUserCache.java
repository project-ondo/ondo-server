package project.team.ondo.domain.user.cache;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.user.data.UserRecommendCacheValue;
import project.team.ondo.domain.user.data.response.UserRecommendItemResponse;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.exception.UserNotFoundException;
import project.team.ondo.domain.user.repository.UserRepository;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RecommendUserCache {

    public static final String CACHE_NAME = "user-recommend";
    public static final String KEY_PREFIX = "v1:user:";

    private final UserRepository userRepository;

    @Cacheable(
            cacheNames = CACHE_NAME,
            key = "T(project.team.ondo.domain.user.cache.RecommendUserCache).key(#mePublicId, #pageable)"
    )
    @Transactional(readOnly = true)
    public UserRecommendCacheValue get(UUID mePublicId, Pageable pageable) {
        UserEntity me = userRepository.findByPublicId(mePublicId)
                .orElseThrow(UserNotFoundException::new);

        Page<@NonNull UserEntity> page = userRepository.recommend(me, pageable);

        List<UserRecommendItemResponse> mapped = page.getContent().stream()
                .map(user -> new UserRecommendItemResponse(
                        user.getPublicId(),
                        user.getDisplayName(),
                        user.getGender(),
                        user.getMajor(),
                        List.copyOf(user.getInterests()),
                        user.getProfileImageKey()
                ))
                .toList();

        return new UserRecommendCacheValue(mapped, page.getTotalElements());
    }

    public static String key(UUID mePublicId, Pageable pageable) {
        return KEY_PREFIX + mePublicId + ":p:" + pageable.getPageNumber() + ":s:" + pageable.getPageSize();
    }
}
