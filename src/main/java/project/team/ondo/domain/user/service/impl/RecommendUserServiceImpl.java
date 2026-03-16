package project.team.ondo.domain.user.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.user.cache.RecommendUserCache;
import project.team.ondo.domain.user.data.UserRecommendCacheValue;
import project.team.ondo.domain.user.data.response.UserRecommendItemResponse;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.service.RecommendUserService;

@Service
@RequiredArgsConstructor
public class RecommendUserServiceImpl implements RecommendUserService {

    private final RecommendUserCache recommendUserCache;

    @Transactional(readOnly = true)
    @Override
    public Page<@NonNull UserRecommendItemResponse> execute(UserEntity me, Pageable pageable) {

        UserRecommendCacheValue cached = recommendUserCache.get(me.getPublicId(), pageable);

        return new PageImpl<>(cached.items(), pageable, cached.totalElements());
    }
}