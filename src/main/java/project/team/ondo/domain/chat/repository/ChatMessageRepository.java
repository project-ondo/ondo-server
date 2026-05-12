package project.team.ondo.domain.chat.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.chat.entity.ChatMessageEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<@NonNull ChatMessageEntity, @NonNull Long>, ChatMessageQueryRepository {
    Page<@NonNull ChatMessageEntity> findAllByRoomIdOrderByIdDesc(Long roomId, Pageable pageable);

    Page<@NonNull ChatMessageEntity> findAllByRoomIdAndIdLessThanOrderByIdDesc(Long roomId, Long cursor, Pageable pageable);

    Optional<ChatMessageEntity> findTopByRoomIdOrderByIdDesc(Long roomId);

    boolean existsByRoomId(Long roomId);

    @Modifying
    @Query("DELETE FROM ChatMessageEntity m WHERE m.roomId = :roomId")
    void deleteAllByRoomId(@Param("roomId") Long roomId);

    @Modifying
    @Query("DELETE FROM ChatMessageEntity m WHERE m.roomId IN :roomIds")
    void deleteAllByRoomIdIn(@Param("roomIds") List<Long> roomIds);
}
