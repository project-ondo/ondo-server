package project.team.ondo.domain.community.comment.service;

import project.team.ondo.domain.community.comment.data.request.CreateCommentRequest;

public interface CreateCommentService {
    void execute(Long postId, CreateCommentRequest request);
}
