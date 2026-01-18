package project.team.ondo.domain.community.post.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.community.post.data.request.CreatePostRequest;
import project.team.ondo.domain.community.post.entity.PostEntity;
import project.team.ondo.domain.community.post.repository.PostRepository;
import project.team.ondo.domain.community.post.service.CreatePostService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.global.security.jwt.service.CurrentUserProvider;

@Service
@RequiredArgsConstructor
public class CreatePostServiceImpl implements CreatePostService {

    private final PostRepository postRepository;
    private final CurrentUserProvider currentUserProvider;

    @Transactional
    @Override
    public Long execute(CreatePostRequest request) {
        UserEntity me = currentUserProvider.getCurrentUser();
        PostEntity post = PostEntity.create(
                request.title(),
                request.content(),
                me,
                request.tags()
        );

        postRepository.save(post);
        return post.getId();
    }
}
