package project.team.ondo.domain.user.data.response;

import project.team.ondo.domain.user.constant.Gender;
import project.team.ondo.domain.user.constant.UserRole;
import project.team.ondo.domain.user.constant.UserStatus;

import java.util.List;
import java.util.UUID;

public record MyProfileResponse(
        UUID publicId,
        String loginId,
        String email,
        String displayName,
        Gender gender,
        String major,
        List<String> interests,
        String profileImageUrl,
        String bio,
        UserRole role,
        UserStatus status,
        double ratingAverage,
        long ratingCount
) {
}
