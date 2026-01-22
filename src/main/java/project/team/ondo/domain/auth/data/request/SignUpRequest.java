package project.team.ondo.domain.auth.data.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import project.team.ondo.domain.user.constant.Gender;

import java.util.List;

public record SignUpRequest(
        @NotBlank String verificationToken,
        @NotBlank @Size(min=4, max=20) String loginId,
        @NotBlank @Size(min=8, max=50) String password,
        @NotBlank @Size(max=20) String displayName,
        Gender gender,
        @NotBlank @Size(max=20) String major,
        List<@NotBlank @Size(max=20) String> interests,
        String profileImageUrl
) {
}
