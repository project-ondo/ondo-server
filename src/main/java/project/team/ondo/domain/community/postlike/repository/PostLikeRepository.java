package project.team.ondo.domain.community.postlike.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.community.post.entity.PostEntity;
import project.team.ondo.domain.community.postlike.entity.PostLikeEntity;
import project.team.ondo.domain.user.entity.UserEntity;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<@NonNull PostLikeEntity, @NonNull Long> {

    boolean existsByUserAndPost(UserEntity user, PostEntity post);

    Optional<PostLikeEntity> findByUserAndPost(UserEntity user, PostEntity post);
}
