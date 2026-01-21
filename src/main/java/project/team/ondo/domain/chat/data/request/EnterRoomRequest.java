package project.team.ondo.domain.chat.data.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record EnterRoomRequest(
        @NotNull UUID roomId
) {
}
