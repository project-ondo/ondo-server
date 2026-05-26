package project.team.ondo.domain.community.comment.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.community.comment.constant.CommentStatus;
import project.team.ondo.domain.community.comment.entity.CommentEntity;
import project.team.ondo.domain.community.post.entity.PostEntity;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<@NonNull CommentEntity, @NonNull Long> {
    Page<@NonNull CommentEntity> findAllByPostAndStatus(
            PostEntity post,
            CommentStatus status,
            Pageable pageable
    );

    Optional<CommentEntity> findByIdAndStatus(Long id, CommentStatus status);

    @Modifying(flushAutomatically = true)
    @Query("UPDATE CommentEntity c SET c.status = :deleted WHERE c.id = :id AND c.status = :active")
    int softDeleteIfActive(@Param("id") Long id,
                           @Param("deleted") CommentStatus deleted,
                           @Param("active") CommentStatus active);
}
