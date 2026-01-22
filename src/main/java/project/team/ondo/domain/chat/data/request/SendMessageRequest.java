package project.team.ondo.domain.chat.data.request;

import jakarta.validation.constraints.NotNull;
import project.team.ondo.domain.chat.constant.MessageType;

import java.util.UUID;

public record SendMessageRequest(
        @NotNull UUID chatRoomPublicId,
        @NotNull MessageType messageType,
        @NotNull String content
) {
}
