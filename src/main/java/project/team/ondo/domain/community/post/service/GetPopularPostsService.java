package project.team.ondo.domain.community.post.service;

import project.team.ondo.domain.community.post.data.response.PopularPostResponse;

import java.util.List;

public interface GetPopularPostsService {
    List<PopularPostResponse> execute();
}
