package project.team.ondo.domain.community.post.service;

import project.team.ondo.domain.user.entity.UserEntity;

public interface DeletePostService {
    void execute(UserEntity me, Long postId);
}
