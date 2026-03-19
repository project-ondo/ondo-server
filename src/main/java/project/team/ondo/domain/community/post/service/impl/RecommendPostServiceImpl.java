package project.team.ondo.domain.community.post.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.community.post.data.response.PostRecommendItemResponse;
import project.team.ondo.domain.community.post.repository.PostRepository;
import project.team.ondo.domain.community.post.service.RecommendPostService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.exception.UserNotFoundException;
import project.team.ondo.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class RecommendPostServiceImpl implements RecommendPostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<@NonNull PostRecommendItemResponse> execute(UserEntity me, Pageable pageable){
        UserEntity managed = userRepository.findByPublicId(me.getPublicId())
                .orElseThrow(UserNotFoundException::new);
        return postRepository.recommend(managed, pageable);
    }
}
