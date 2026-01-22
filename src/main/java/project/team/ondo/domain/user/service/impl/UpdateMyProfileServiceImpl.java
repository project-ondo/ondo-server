package project.team.ondo.domain.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.auth.exception.DisplayNameDuplicatedException;
import project.team.ondo.domain.user.cache.RecommendUserCacheEvictService;
import project.team.ondo.domain.user.data.request.UpdateMyProfileRequest;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.repository.UserRepository;
import project.team.ondo.domain.user.service.UpdateMyProfileService;
import project.team.ondo.global.security.jwt.service.CurrentUserProvider;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UpdateMyProfileServiceImpl implements UpdateMyProfileService {

    private final CurrentUserProvider currentUserProvider;
    private final UserRepository userRepository;
    private final RecommendUserCacheEvictService recommendUserCacheEvictService;

    @Transactional
    @Override
    public void execute(UpdateMyProfileRequest request) {
        UserEntity me = currentUserProvider.getCurrentUser();

        if (!me.getDisplayName().equals(request.displayName()) && userRepository.existsByDisplayName(request.displayName())) {
            throw new DisplayNameDuplicatedException();
        }

        boolean recommendCriteriaChanged =
                isMajorChanged(me.getMajor(), request.major())
                        || isInterestsChanged(me.getInterests(), request.interests());

        me.updateProfile(
                request.displayName(),
                request.gender(),
                request.major(),
                request.interests(),
                request.bio()
        );

        if (recommendCriteriaChanged) recommendUserCacheEvictService.evict(me.getPublicId());
    }

    private boolean isMajorChanged(String oldMajor, String newMajor) {
        String a = oldMajor == null ? "" : oldMajor.trim();
        String b = newMajor == null ? "" : newMajor.trim();
        return !a.equals(b);
    }

    private boolean isInterestsChanged(List<String> oldInterests, List<String> newInterests) {
        Set<String> a = normalize(oldInterests);
        Set<String> b = normalize(newInterests);
        return !a.equals(b);
    }

    private Set<String> normalize(List<String> interests) {
        if (interests == null) return Collections.emptySet();
        return interests.stream()
                .filter(s -> s != null && !s.isBlank())
                .map(s -> s.trim().toLowerCase())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
