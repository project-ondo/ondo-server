package project.team.ondo.domain.community.post.service;

import project.team.ondo.domain.community.post.data.request.UpdatePostRequest;
import project.team.ondo.domain.user.entity.UserEntity;

public interface UpdatePostService {
    void execute(UserEntity me, Long postId, UpdatePostRequest request);
}
