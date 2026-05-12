package project.team.ondo.domain.rating.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.rating.entity.UserRatingEntity;

@Repository
public interface UserRatingRepository extends JpaRepository<@NonNull UserRatingEntity, @NonNull Long>, UserRatingQueryRepository {
    boolean existsByRoomIdAndRaterIdAndRateeId(Long roomId, Long raterId, Long rateeId);

    @Modifying
    @Query("DELETE FROM UserRatingEntity r WHERE r.raterId = :userId OR r.rateeId = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);
}
