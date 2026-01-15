package project.team.ondo.domain.user.data.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import project.team.ondo.domain.user.constant.Gender;

import java.util.List;

public record UpdateMyProfileRequest(
        @NotBlank @Size(max=20) String displayName,
        Gender gender,
        @NotBlank @Size(max=20) String major,
        List<@NotBlank @Size(max=20) String> interests,
        @Size(max=500) String bio
) {
}
