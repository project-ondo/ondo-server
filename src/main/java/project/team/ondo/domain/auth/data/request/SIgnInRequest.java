package project.team.ondo.domain.auth.data.request;

import jakarta.validation.constraints.NotBlank;

public record SIgnInRequest(
        @NotBlank String uid,
        @NotBlank String password
) {
}
