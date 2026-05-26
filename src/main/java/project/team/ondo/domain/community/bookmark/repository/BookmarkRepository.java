package project.team.ondo.domain.community.bookmark.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.community.bookmark.entity.BookmarkEntity;
import project.team.ondo.domain.community.post.entity.PostEntity;
import project.team.ondo.domain.user.entity.UserEntity;

@Repository
public interface BookmarkRepository extends JpaRepository<@NonNull BookmarkEntity, @NonNull Long> {

    boolean existsByUserAndPost(UserEntity user, PostEntity post);

    @Modifying(flushAutomatically = true)
    @Query("DELETE FROM BookmarkEntity b WHERE b.user = :user AND b.post = :post")
    int deleteByUserAndPost(@Param("user") UserEntity user, @Param("post") PostEntity post);

    @Modifying
    @Query("DELETE FROM BookmarkEntity b WHERE b.post = :post")
    void deleteAllByPost(@Param("post") PostEntity post);

    @Query(
            value = "SELECT b FROM BookmarkEntity b JOIN FETCH b.post p JOIN FETCH p.author WHERE b.user = :user",
            countQuery = "SELECT COUNT(b) FROM BookmarkEntity b WHERE b.user = :user"
    )
    Page<BookmarkEntity> findAllByUserWithPost(@Param("user") UserEntity user, Pageable pageable);
}
