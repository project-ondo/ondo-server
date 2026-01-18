package project.team.ondo.domain.community.post.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.community.post.data.response.PostRecommendItemResponse;
import project.team.ondo.domain.user.entity.UserEntity;

@Repository
public interface PostRecommendRepository {
    Page<@NonNull PostRecommendItemResponse> recommend(UserEntity me, Pageable pageable);
}
