package project.team.ondo.domain.community.post.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.community.post.constant.PostStatus;
import project.team.ondo.domain.community.post.entity.PostEntity;
import project.team.ondo.domain.community.post.exception.PostNotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<@NonNull PostEntity, @NonNull Long>, PostRecommendQueryRepository, PostSearchQueryRepository {

    Optional<PostEntity> findByIdAndStatus(Long id, PostStatus status);

    Page<@NonNull PostEntity> findAllByStatus(PostStatus status, Pageable pageable);

    @Query("SELECT DISTINCT p FROM PostEntity p JOIN FETCH p.author LEFT JOIN FETCH p.tags WHERE p.id IN :ids")
    List<PostEntity> findAllByIdWithDetails(@Param("ids") Collection<Long> ids);

    default PostEntity getActiveById(Long id) {
        return findByIdAndStatus(id, PostStatus.ACTIVE).orElseThrow(PostNotFoundException::new);
    }
}
