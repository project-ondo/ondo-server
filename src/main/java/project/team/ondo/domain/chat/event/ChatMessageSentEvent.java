package project.team.ondo.domain.chat.event;

import jakarta.validation.constraints.NotNull;
import project.team.ondo.domain.chat.entity.ChatMessageEntity;

public record ChatMessageSentEvent(
        @NotNull ChatMessageEntity message
) {
}
