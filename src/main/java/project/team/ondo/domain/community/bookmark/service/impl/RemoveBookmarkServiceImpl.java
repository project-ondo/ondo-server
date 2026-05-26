package project.team.ondo.domain.community.bookmark.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.community.bookmark.repository.BookmarkRepository;
import project.team.ondo.domain.community.bookmark.service.RemoveBookmarkService;
import project.team.ondo.domain.community.post.entity.PostEntity;
import project.team.ondo.domain.community.post.repository.PostCommandRepository;
import project.team.ondo.domain.community.post.repository.PostRepository;
import project.team.ondo.domain.user.entity.UserEntity;

@Service
@RequiredArgsConstructor
public class RemoveBookmarkServiceImpl implements RemoveBookmarkService {

    private final PostRepository postRepository;
    private final PostCommandRepository postCommandRepository;
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    @Override
    public void execute(UserEntity me, Long postId) {
        PostEntity post = postRepository.getActiveById(postId);

        int removed = bookmarkRepository.deleteByUserAndPost(me, post);

        if (removed > 0) postCommandRepository.decreaseBookmarkCount(postId);
    }
}
