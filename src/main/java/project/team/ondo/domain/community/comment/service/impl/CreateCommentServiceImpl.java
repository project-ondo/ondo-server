package project.team.ondo.domain.community.comment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.community.comment.data.request.CreateCommentRequest;
import project.team.ondo.domain.community.comment.entity.CommentEntity;
import project.team.ondo.domain.community.comment.event.PostCommentCreatedEvent;
import project.team.ondo.domain.community.comment.repository.CommentRepository;
import project.team.ondo.domain.community.comment.service.CreateCommentService;
import project.team.ondo.domain.community.post.entity.PostEntity;
import project.team.ondo.domain.community.post.repository.PostRepository;
import project.team.ondo.domain.user.entity.UserEntity;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateCommentServiceImpl implements CreateCommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public void execute(UserEntity me, Long postId, CreateCommentRequest request){

        PostEntity post = postRepository.getActiveById(postId);

        CommentEntity comment = CommentEntity.create(
                request.content(),
                post,
                me
        );

        commentRepository.save(comment);

        UUID receiverPublicId = post.getAuthor().getPublicId();

        if (!receiverPublicId.equals(me.getPublicId())) {
            eventPublisher.publishEvent(
                    new PostCommentCreatedEvent(
                            post.getId(),
                            comment.getId(),
                            me.getPublicId(),
                            receiverPublicId
                    )
            );
        }

        post.increaseCommentCount();
    }
}
