package project.team.ondo.domain.community.post.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.community.post.constant.PostStatus;
import project.team.ondo.domain.community.post.entity.PostEntity;
import project.team.ondo.domain.community.post.exception.PostNotFoundException;
import project.team.ondo.domain.community.post.repository.PostRepository;
import project.team.ondo.domain.community.post.service.DeletePostService;
import project.team.ondo.global.security.jwt.service.CurrentUserProvider;

@Service
@RequiredArgsConstructor
public class DeletePostServiceImpl implements DeletePostService {

    private final PostRepository postRepository;
    private final CurrentUserProvider currentUserProvider;

    @Transactional
    @Override
    public void execute(Long postId) {
        PostEntity post = postRepository.findByIdAndStatus(postId, PostStatus.ACTIVE)
                .orElseThrow(PostNotFoundException::new);

        if (!post.getAuthor().getPublicId().equals(currentUserProvider.getCurrentUser().getPublicId())) {
            throw new AccessDeniedException("게시글 삭제 권한이 없습니다.");
        }

        post.delete();
    }
}
