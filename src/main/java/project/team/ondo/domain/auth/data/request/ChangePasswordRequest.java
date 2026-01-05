package project.team.ondo.domain.auth.data.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(
        @Email @NotBlank String email,
        @NotBlank String password
) {
}
