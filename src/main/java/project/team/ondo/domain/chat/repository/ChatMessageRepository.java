package project.team.ondo.domain.chat.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.team.ondo.domain.chat.entity.ChatMessageEntity;

@Repository
public interface ChatMessageRepository extends JpaRepository<@NonNull ChatMessageEntity, @NonNull Long>, ChatMessageQueryRepository {
}
