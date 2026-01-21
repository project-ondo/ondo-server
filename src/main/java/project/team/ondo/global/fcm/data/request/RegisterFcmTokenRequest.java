package project.team.ondo.global.fcm.data.request;

import jakarta.validation.constraints.NotBlank;

public record RegisterFcmTokenRequest(
        @NotBlank String token
) {
}
