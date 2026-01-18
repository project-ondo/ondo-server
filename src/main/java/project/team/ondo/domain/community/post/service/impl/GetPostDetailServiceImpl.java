package project.team.ondo.domain.community.post.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.community.post.constant.PostStatus;
import project.team.ondo.domain.community.post.data.response.PostDetailResponse;
import project.team.ondo.domain.community.post.entity.PostEntity;
import project.team.ondo.domain.community.post.exception.PostNotFoundException;
import project.team.ondo.domain.community.post.repository.PostRepository;
import project.team.ondo.domain.community.post.service.GetPostDetailService;

@Service
@RequiredArgsConstructor
public class GetPostDetailServiceImpl implements GetPostDetailService {

    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    @Override
    public PostDetailResponse execute(Long postId) {
        PostEntity post = postRepository.findByIdAndStatus(postId, PostStatus.ACTIVE)
                .orElseThrow(PostNotFoundException::new);

        post.incrementViewCount();

        return PostDetailResponse.from(post);
    }
}
