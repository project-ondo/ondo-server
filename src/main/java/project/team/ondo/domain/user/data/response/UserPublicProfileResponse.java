package project.team.ondo.domain.user.data.response;

import project.team.ondo.domain.user.constant.Gender;
import project.team.ondo.domain.user.constant.UserRole;

import java.util.List;
import java.util.UUID;

public record UserPublicProfileResponse(
        UUID publicId,
        String displayName,
        Gender gender,
        String major,
        List<String> interests,
        String profileImageUrl,
        String bio,
        UserRole role
) {
}
