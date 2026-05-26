package project.team.ondo.domain.community.post.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.community.post.entity.PostEntity;

@Repository
public interface PostCommandRepository extends org.springframework.data.repository.Repository<PostEntity, Long> {

    @Modifying(flushAutomatically = true)
    @Query("UPDATE PostEntity p SET p.viewCount = p.viewCount + 1 WHERE p.id = :id")
    void incrementViewCount(@Param("id") Long id);

    @Modifying(flushAutomatically = true)
    @Query("UPDATE PostEntity p SET p.likeCount = p.likeCount + 1 WHERE p.id = :id")
    void incrementLikeCount(@Param("id") Long id);

    @Modifying(flushAutomatically = true)
    @Query("UPDATE PostEntity p SET p.likeCount = p.likeCount - 1 WHERE p.id = :id AND p.likeCount > 0")
    void decreaseLikeCount(@Param("id") Long id);

    @Modifying(flushAutomatically = true)
    @Query("UPDATE PostEntity p SET p.commentCount = p.commentCount + 1 WHERE p.id = :id")
    void incrementCommentCount(@Param("id") Long id);

    @Modifying(flushAutomatically = true)
    @Query("UPDATE PostEntity p SET p.commentCount = p.commentCount - 1 WHERE p.id = :id AND p.commentCount > 0")
    void decreaseCommentCount(@Param("id") Long id);

    @Modifying(flushAutomatically = true)
    @Query("UPDATE PostEntity p SET p.bookmarkCount = p.bookmarkCount + 1 WHERE p.id = :id")
    void incrementBookmarkCount(@Param("id") Long id);

    @Modifying(flushAutomatically = true)
    @Query("UPDATE PostEntity p SET p.bookmarkCount = p.bookmarkCount - 1 WHERE p.id = :id AND p.bookmarkCount > 0")
    void decreaseBookmarkCount(@Param("id") Long id);
}
