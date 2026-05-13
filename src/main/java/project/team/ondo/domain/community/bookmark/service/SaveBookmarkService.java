package project.team.ondo.domain.community.bookmark.service;

import project.team.ondo.domain.user.entity.UserEntity;

public interface SaveBookmarkService {
    void execute(UserEntity me, Long postId);
}
