package project.team.ondo.domain.community.post.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.community.post.entity.PostEntity;
import project.team.ondo.domain.community.post.repository.PostRepository;
import project.team.ondo.domain.community.post.service.DeletePostService;
import project.team.ondo.domain.user.entity.UserEntity;

@Service
@RequiredArgsConstructor
public class DeletePostServiceImpl implements DeletePostService {

    private final PostRepository postRepository;

    @Transactional
    @Override
    public void execute(UserEntity me, Long postId) {
        PostEntity post = postRepository.getActiveById(postId);

        post.requireAuthor(me.getPublicId());
        post.delete();
    }
}
