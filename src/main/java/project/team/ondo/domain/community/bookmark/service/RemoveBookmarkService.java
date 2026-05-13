package project.team.ondo.domain.community.bookmark.service;

import project.team.ondo.domain.user.entity.UserEntity;

public interface RemoveBookmarkService {
    void execute(UserEntity me, Long postId);
}
