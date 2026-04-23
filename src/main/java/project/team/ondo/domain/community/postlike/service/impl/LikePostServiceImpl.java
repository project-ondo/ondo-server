package project.team.ondo.domain.community.postlike.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.community.post.entity.PostEntity;
import project.team.ondo.domain.community.post.repository.PostRepository;
import project.team.ondo.domain.community.postlike.entity.PostLikeEntity;
import project.team.ondo.domain.community.postlike.event.PostLikedEvent;
import project.team.ondo.domain.community.postlike.exception.AlreadyLikedException;
import project.team.ondo.domain.community.postlike.repository.PostLikeRepository;
import project.team.ondo.domain.community.postlike.service.LikePostService;
import project.team.ondo.domain.user.entity.UserEntity;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LikePostServiceImpl implements LikePostService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public void execute(UserEntity me, Long postId) {

        PostEntity post = postRepository.getActiveById(postId);

        if (postLikeRepository.existsByUserAndPost(me, post)) throw new AlreadyLikedException();

        postLikeRepository.save(PostLikeEntity.create(me, post));
        post.incrementLikeCount();

        UUID receiverPublicId = post.getAuthor().getPublicId();
        if (!receiverPublicId.equals(me.getPublicId())) {
            eventPublisher.publishEvent(
                    new PostLikedEvent(
                            postId,
                            me.getPublicId(),
                            receiverPublicId
                    )
            );
        }
    }
}
