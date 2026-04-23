package project.team.ondo.domain.community.post.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.community.post.data.request.SearchPostRequest;
import project.team.ondo.domain.community.post.data.response.PostRecommendItemResponse;
import project.team.ondo.domain.community.post.repository.PostRepository;
import project.team.ondo.domain.community.post.service.SearchPostService;

@Service
@RequiredArgsConstructor
public class SearchPostServiceImpl implements SearchPostService {

    private final PostRepository postRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<@NonNull PostRecommendItemResponse> execute(SearchPostRequest request, Pageable pageable) {
        return postRepository.search(request, pageable);
    }
}