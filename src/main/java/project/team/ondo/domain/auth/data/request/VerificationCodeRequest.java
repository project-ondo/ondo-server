package project.team.ondo.domain.auth.data.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VerificationCodeRequest(
        @NotBlank @Size(min=8, max=8) String code
        ) {
}
