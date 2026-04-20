package project.team.ondo.domain.community.comment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.community.comment.constant.CommentStatus;
import project.team.ondo.domain.community.comment.entity.CommentEntity;
import project.team.ondo.domain.community.comment.exception.CommentNotFoundException;
import project.team.ondo.domain.community.comment.repository.CommentRepository;
import project.team.ondo.domain.community.comment.service.DeleteCommentService;
import project.team.ondo.domain.user.entity.UserEntity;

@Service
@RequiredArgsConstructor
public class DeleteCommentServiceImpl implements DeleteCommentService {

    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public void execute(UserEntity me, Long commentId) {
        CommentEntity comment = commentRepository.findByIdAndStatus(commentId, CommentStatus.ACTIVE)
                .orElseThrow(CommentNotFoundException::new);

        comment.requireAuthor(me.getPublicId());
        comment.delete();
        comment.getPost().decreaseCommentCount();
    }
}
