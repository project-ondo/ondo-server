package project.team.ondo.domain.community.postlike.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.community.post.entity.PostEntity;
import project.team.ondo.domain.community.post.repository.PostCommandRepository;
import project.team.ondo.domain.community.post.repository.PostRepository;
import project.team.ondo.domain.community.postlike.repository.PostLikeRepository;
import project.team.ondo.domain.community.postlike.service.UnlikePostService;
import project.team.ondo.domain.user.entity.UserEntity;

@Service
@RequiredArgsConstructor
public class UnlikePostServiceImpl implements UnlikePostService {

    private final PostRepository postRepository;
    private final PostCommandRepository postCommandRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    @Override
    public void execute(UserEntity me, Long postId) {

        PostEntity post = postRepository.getActiveById(postId);

        int removed = postLikeRepository.deleteByUserAndPost(me, post);

        if (removed > 0) postCommandRepository.decreaseLikeCount(postId);
    }
}
