package project.team.ondo.domain.community.post.service;

import project.team.ondo.domain.community.post.data.response.PostDetailResponse;

public interface GetPostDetailService {
    PostDetailResponse execute(Long postId);
}
