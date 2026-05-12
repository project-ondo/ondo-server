package project.team.ondo.domain.chat.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.chat.entity.ChatRoomMemberEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<@NonNull ChatRoomMemberEntity, @NonNull Long>, ChatRoomMemberCommandRepository {
    Optional<ChatRoomMemberEntity> findByRoomIdAndUserId(Long roomId, Long userId);
    List<ChatRoomMemberEntity> findAllByRoomId(Long roomId);

    @Modifying
    @Query("DELETE FROM ChatRoomMemberEntity m WHERE m.roomId = :roomId")
    void deleteAllByRoomId(@Param("roomId") Long roomId);

    @Modifying
    @Query("DELETE FROM ChatRoomMemberEntity m WHERE m.roomId IN :roomIds")
    void deleteAllByRoomIdIn(@Param("roomIds") List<Long> roomIds);
}
