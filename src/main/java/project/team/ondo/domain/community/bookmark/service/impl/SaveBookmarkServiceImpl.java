package project.team.ondo.domain.community.bookmark.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.community.bookmark.entity.BookmarkEntity;
import project.team.ondo.domain.community.bookmark.exception.AlreadyBookmarkedException;
import project.team.ondo.domain.community.bookmark.repository.BookmarkRepository;
import project.team.ondo.domain.community.bookmark.service.SaveBookmarkService;
import project.team.ondo.domain.community.post.entity.PostEntity;
import project.team.ondo.domain.community.post.repository.PostRepository;
import project.team.ondo.domain.user.entity.UserEntity;

@Service
@RequiredArgsConstructor
public class SaveBookmarkServiceImpl implements SaveBookmarkService {

    private final PostRepository postRepository;
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    @Override
    public void execute(UserEntity me, Long postId) {
        PostEntity post = postRepository.getActiveById(postId);

        if (bookmarkRepository.existsByUserAndPost(me, post)) throw new AlreadyBookmarkedException();

        bookmarkRepository.save(BookmarkEntity.create(me, post));
        post.incrementBookmarkCount();
    }
}
