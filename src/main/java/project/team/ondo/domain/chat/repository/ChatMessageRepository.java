package project.team.ondo.domain.chat.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.chat.entity.ChatMessageEntity;

import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<@NonNull ChatMessageEntity, @NonNull Long>, ChatMessageQueryRepository {
    Page<@NonNull ChatMessageEntity> findAllByRoomIdOrderByIdDesc(Long roomId, Pageable pageable);

    Page<@NonNull ChatMessageEntity> findAllByRoomIdAndIdLessThanOrderByIdDesc(Long roomId, Long cursor, Pageable pageable);

    Optional<ChatMessageEntity> findTopByRoomIdOrderByIdDesc(Long roomId);
}
