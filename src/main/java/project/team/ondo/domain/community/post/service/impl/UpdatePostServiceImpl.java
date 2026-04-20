package project.team.ondo.domain.community.post.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.community.post.data.request.UpdatePostRequest;
import project.team.ondo.domain.community.post.entity.PostEntity;
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
        PostEntity post = postRepository.getActiveById(postId);

        post.requireAuthor(me.getPublicId());
        post.update(
                request.title(),
                request.content(),
                request.tags()
        );
    }
}
