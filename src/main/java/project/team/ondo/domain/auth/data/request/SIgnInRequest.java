package project.team.ondo.domain.auth.data.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SIgnInRequest(
        @NotBlank @Size(min=4, max=20) String loginId,
        @NotBlank @Size(min=8, max=50) String password
) {
}
