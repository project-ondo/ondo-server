package project.team.ondo.domain.community.postlike.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.community.post.entity.PostEntity;
import project.team.ondo.domain.community.postlike.entity.PostLikeEntity;
import project.team.ondo.domain.user.entity.UserEntity;

@Repository
public interface PostLikeRepository extends JpaRepository<@NonNull PostLikeEntity, @NonNull Long> {

    boolean existsByUserAndPost(UserEntity user, PostEntity post);

    @Modifying
    @Query("DELETE FROM PostLikeEntity pl WHERE pl.user = :user AND pl.post = :post")
    int deleteByUserAndPost(@Param("user") UserEntity user, @Param("post") PostEntity post);
}
