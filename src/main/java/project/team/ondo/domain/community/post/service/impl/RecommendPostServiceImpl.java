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

@Service
@RequiredArgsConstructor
public class RecommendPostServiceImpl implements RecommendPostService {

    private final PostRepository postRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<@NonNull PostRecommendItemResponse> execute(UserEntity me, Pageable pageable){
        return postRepository.recommend(me, pageable);
    }
}
