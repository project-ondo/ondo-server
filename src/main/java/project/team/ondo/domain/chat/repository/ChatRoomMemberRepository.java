package project.team.ondo.domain.chat.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.chat.entity.ChatRoomMemberEntity;

import java.util.Optional;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<@NonNull ChatRoomMemberEntity, @NonNull Long> {
    Optional<ChatRoomMemberEntity> findByRoomIdAndUserId(Long roomId, Long userId);
}
