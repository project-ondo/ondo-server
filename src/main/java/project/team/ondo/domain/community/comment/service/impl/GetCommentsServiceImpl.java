package project.team.ondo.domain.community.comment.service.impl;

import jakarta.annotation.Nonnull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.community.comment.constant.CommentStatus;
import project.team.ondo.domain.community.comment.data.response.CommentItemResponse;
import project.team.ondo.domain.community.comment.repository.CommentRepository;
import project.team.ondo.domain.community.comment.service.GetCommentsService;
import project.team.ondo.domain.community.post.entity.PostEntity;
import project.team.ondo.domain.community.post.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class GetCommentsServiceImpl implements GetCommentsService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<@NonNull CommentItemResponse> execute(Long postId, Pageable pageable) {
        PostEntity post = postRepository.getActiveById(postId);

        return commentRepository.findAllByPostAndStatus(post, CommentStatus.ACTIVE, pageable)
                .map(CommentItemResponse::from);
    }
}
