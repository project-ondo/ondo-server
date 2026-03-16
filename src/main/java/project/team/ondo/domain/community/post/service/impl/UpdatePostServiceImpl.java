package project.team.ondo.domain.community.post.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.community.post.constant.PostStatus;
import project.team.ondo.domain.community.post.data.request.UpdatePostRequest;
import project.team.ondo.domain.community.post.entity.PostEntity;
import project.team.ondo.domain.community.post.exception.PostNotFoundException;
import project.team.ondo.domain.community.post.repository.PostRepository;
import project.team.ondo.domain.community.post.service.UpdatePostService;
import project.team.ondo.domain.user.entity.UserEntity;

@Service
@RequiredArgsConstructor
public class UpdatePostServiceImpl implements UpdatePostService {

    private final PostRepository postRepository;

    @Transactional
    @Override
    public void execute(UserEntity me, Long postId, UpdatePostRequest request) {
        PostEntity post = postRepository.findByIdAndStatus(postId, PostStatus.ACTIVE)
                .orElseThrow(PostNotFoundException::new);

        if (!post.getAuthor().getPublicId().equals(me.getPublicId())) {
            throw new AccessDeniedException("게시글 수정 권한이 없습니다.");
        }

        post.update(
                request.title(),
                request.content(),
                request.tags()
        );
    }
}
