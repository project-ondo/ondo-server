package project.team.ondo.domain.user.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.user.data.response.UserRecommendItemResponse;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.repository.UserRepository;
import project.team.ondo.domain.user.service.RecommendUserService;
import project.team.ondo.global.security.jwt.service.CurrentUserProvider;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendUserServiceImpl implements RecommendUserService {

    private final UserRepository userRepository;
    private final CurrentUserProvider currentUserProvider;

    @Transactional(readOnly = true)
    @Override
    public Page<@NonNull UserRecommendItemResponse> execute(Pageable pageable) {
        UserEntity me = currentUserProvider.getCurrentUser();

        Page<@NonNull UserEntity> page = userRepository.recommend(me, pageable);

        List<UserRecommendItemResponse> mapped = page.getContent().stream()
                .map(user -> new UserRecommendItemResponse(
                        user.getPublicId(),
                        user.getDisplayName(),
                        user.getGender(),
                        user.getMajor(),
                        new ArrayList<>(user.getInterests()),
                        user.getProfileImageUrl()
                ))
                .toList();

        return new PageImpl<>(mapped, pageable, page.getTotalElements());
    }
}
