package project.team.ondo.domain.community.post.service;

import project.team.ondo.domain.community.post.data.request.UpdatePostRequest;

public interface UpdatePostService {
    void execute(Long postId, UpdatePostRequest request);
}
