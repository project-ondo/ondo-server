package project.team.ondo.domain.community.post.service;

import project.team.ondo.domain.community.post.data.request.CreatePostRequest;
import project.team.ondo.domain.user.entity.UserEntity;

public interface CreatePostService {
    Long execute(UserEntity me, CreatePostRequest request);
}
