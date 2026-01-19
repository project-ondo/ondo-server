package project.team.ondo.domain.community.comment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.community.comment.constant.CommentStatus;
import project.team.ondo.domain.community.comment.entity.CommentEntity;
import project.team.ondo.domain.community.comment.exception.CommentNotFoundException;
import project.team.ondo.domain.community.comment.repository.CommentRepository;
import project.team.ondo.domain.community.comment.service.DeleteCommentService;
import project.team.ondo.global.security.jwt.service.CurrentUserProvider;

@Service
@RequiredArgsConstructor
public class DeleteCommentServiceImpl implements DeleteCommentService {

    private final CommentRepository commentRepository;
    private final CurrentUserProvider currentUserProvider;

    @Transactional
    @Override
    public void execute(Long commentId) {
        CommentEntity comment = commentRepository.findByIdAndStatus(commentId, CommentStatus.ACTIVE)
                .orElseThrow(CommentNotFoundException::new);

        if (!comment.getAuthor().getPublicId().equals(currentUserProvider.getCurrentUser().getPublicId())) {
            throw new AccessDeniedException("댓글 삭제 권한이 없습니다.");
        }

        comment.delete();
        comment.getPost().decreaseCommentCount();
    }
}
