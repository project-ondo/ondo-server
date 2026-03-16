package project.team.ondo.domain.community.comment.service;

import project.team.ondo.domain.user.entity.UserEntity;

public interface DeleteCommentService {
    void execute(UserEntity me, Long commentId);
}
