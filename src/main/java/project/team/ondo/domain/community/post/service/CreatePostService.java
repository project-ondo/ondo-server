package project.team.ondo.domain.community.post.service;

import project.team.ondo.domain.community.post.data.request.CreatePostRequest;

public interface CreatePostService {
    Long execute(CreatePostRequest request);
}
