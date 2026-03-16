package project.team.ondo.domain.user.data.response;

import project.team.ondo.domain.user.constant.Gender;
import project.team.ondo.domain.user.constant.UserRole;
import project.team.ondo.domain.user.entity.UserEntity;

import java.util.List;
import java.util.UUID;

public record UserPublicProfileResponse(
        UUID publicId,
        String displayName,
        Gender gender,
        String major,
        List<String> interests,
        String profileImageKey,
        String bio,
        UserRole role,
        double ratingAverage,
        long ratingCount
) {
    public static UserPublicProfileResponse from(UserEntity user) {
        return new UserPublicProfileResponse(
                user.getPublicId(),
                user.getDisplayName(),
                user.getGender(),
                user.getMajor(),
                user.getInterests(),
                user.getProfileImageKey(),
                user.getBio(),
                user.getRole(),
                user.getRatingAvg(),
                user.getRatingCount()
        );
    }
}
