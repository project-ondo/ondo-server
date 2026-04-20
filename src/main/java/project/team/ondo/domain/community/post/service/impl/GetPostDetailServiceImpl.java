package project.team.ondo.domain.community.post.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.community.post.data.response.PostDetailResponse;
import project.team.ondo.domain.community.post.entity.PostEntity;
import project.team.ondo.domain.community.post.repository.PostRepository;
import project.team.ondo.domain.community.post.service.GetPostDetailService;

@Service
@RequiredArgsConstructor
public class GetPostDetailServiceImpl implements GetPostDetailService {

    private final PostRepository postRepository;

    @Transactional
    @Override
    public PostDetailResponse execute(Long postId) {
        PostEntity post = postRepository.getActiveById(postId);

        post.incrementViewCount();

        return PostDetailResponse.from(post);
    }
}
