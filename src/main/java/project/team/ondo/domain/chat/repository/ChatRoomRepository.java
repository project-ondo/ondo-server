package project.team.ondo.domain.chat.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.chat.entity.ChatRoomEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRoomRepository extends JpaRepository<@NonNull ChatRoomEntity, @NonNull Long>, ChatRoomQueryRepository {
    Optional<ChatRoomEntity> findByPublicId(UUID publicId);

    Optional<ChatRoomEntity> findByUserAIdAndUserBId(Long userAId, Long userBId);
}
