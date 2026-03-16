package project.team.ondo.domain.user.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.user.data.request.UserSearchCondition;
import project.team.ondo.domain.user.data.response.UserRecommendItemResponse;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.repository.UserRepository;
import project.team.ondo.domain.user.service.SearchUserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchUserServiceImpl implements SearchUserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<@NonNull UserRecommendItemResponse> execute(UserSearchCondition condition, Pageable pageable) {
        Page<@NonNull UserEntity> result = userRepository.searchUser(condition, pageable);

        List<UserRecommendItemResponse> mapped = result.getContent().stream()
                .map(UserRecommendItemResponse::from)
                .toList();

        return new PageImpl<>(mapped, pageable, result.getTotalElements());
    }
}
