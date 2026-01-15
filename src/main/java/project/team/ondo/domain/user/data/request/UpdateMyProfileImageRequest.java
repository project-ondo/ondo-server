package project.team.ondo.domain.user.data.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateMyProfileImageRequest(
        @NotBlank String key
) {
}
