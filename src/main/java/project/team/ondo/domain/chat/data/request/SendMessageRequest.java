package project.team.ondo.domain.chat.data.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import project.team.ondo.domain.chat.constant.MessageType;

import java.util.UUID;

public record SendMessageRequest(
        @NotNull UUID chatRoomPublicId,
        @NotNull MessageType messageType,
        @NotNull @Size(min = 1, max = 2000) String content
) {
}
