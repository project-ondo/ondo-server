package project.team.ondo.domain.community.postlike.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.community.post.constant.PostStatus;
import project.team.ondo.domain.community.post.entity.PostEntity;
import project.team.ondo.domain.community.post.exception.PostNotFoundException;
import project.team.ondo.domain.community.post.repository.PostRepository;
import project.team.ondo.domain.community.postlike.entity.PostLikeEntity;
import project.team.ondo.domain.community.postlike.repository.PostLikeRepository;
import project.team.ondo.domain.community.postlike.service.LikePostService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.global.security.jwt.service.CurrentUserProvider;

@Service
@RequiredArgsConstructor
public class LikePostServiceImpl implements LikePostService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final CurrentUserProvider currentUserProvider;

    @Transactional
    @Override
    public void execute(Long postId) {
        UserEntity me = currentUserProvider.getCurrentUser();

        PostEntity post = postRepository.findByIdAndStatus(postId, PostStatus.ACTIVE)
                .orElseThrow(PostNotFoundException::new);

        if (postLikeRepository.existsByUserAndPost(me, post)) return;

        postLikeRepository.save(PostLikeEntity.create(me, post));
        post.incrementLikeCount();
    }
}
