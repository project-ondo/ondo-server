package project.team.ondo.domain.community.comment.service;

import project.team.ondo.domain.community.comment.data.request.CreateCommentRequest;
import project.team.ondo.domain.user.entity.UserEntity;

public interface CreateCommentService {
    void execute(UserEntity me, Long postId, CreateCommentRequest request);
}
