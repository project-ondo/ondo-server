package project.team.ondo.domain.chat.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.chat.entity.ChatRoomEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRoomRepository extends JpaRepository<@NonNull ChatRoomEntity, @NonNull Long>, ChatRoomQueryRepository {
    Optional<ChatRoomEntity> findByPublicId(UUID publicId);

    Optional<ChatRoomEntity> findByUserAIdAndUserBId(Long userAId, Long userBId);

    @Query("SELECT r FROM ChatRoomEntity r WHERE r.userAId = :userId OR r.userBId = :userId")
    List<ChatRoomEntity> findAllByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM ChatRoomEntity r WHERE r.userAId = :userId OR r.userBId = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);
}
