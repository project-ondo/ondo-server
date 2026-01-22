package project.team.ondo.domain.rating.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.rating.entity.UserRatingEntity;

@Repository
public interface UserRatingRepository extends JpaRepository<@NonNull UserRatingEntity, @NonNull Long> {
    boolean existsByRoomIdAndRaterIdAndRateeId(Long roomId, Long raterId, Long rateeId);
}
