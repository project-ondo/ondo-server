package project.team.ondo.domain.community.post.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.community.post.constant.PostStatus;
import project.team.ondo.domain.community.post.entity.PostEntity;
import project.team.ondo.domain.community.post.exception.PostNotFoundException;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<@NonNull PostEntity, @NonNull Long>, PostRecommendQueryRepository, PostSearchQueryRepository {

    Optional<PostEntity> findByIdAndStatus(Long id, PostStatus status);

    Page<@NonNull PostEntity> findAllByStatus(PostStatus status, Pageable pageable);

    default PostEntity getActiveById(Long id) {
        return findByIdAndStatus(id, PostStatus.ACTIVE).orElseThrow(PostNotFoundException::new);
    }
}
