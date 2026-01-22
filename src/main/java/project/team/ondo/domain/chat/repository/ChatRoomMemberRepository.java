package project.team.ondo.domain.chat.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.chat.entity.ChatRoomMemberEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<@NonNull ChatRoomMemberEntity, @NonNull Long>, ChatRoomMemberCommandRepository {
    Optional<ChatRoomMemberEntity> findByRoomIdAndUserId(Long roomId, Long userId);
    List<ChatRoomMemberEntity> findAllByRoomId(Long roomId);
}
