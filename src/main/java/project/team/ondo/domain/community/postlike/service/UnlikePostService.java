package project.team.ondo.domain.community.postlike.service;

import project.team.ondo.domain.user.entity.UserEntity;

public interface UnlikePostService {
    void execute(UserEntity me, Long postId);
}
