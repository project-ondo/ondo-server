package project.team.ondo.domain.user.data.response;

import project.team.ondo.global.constant.Gender;
import project.team.ondo.domain.user.constant.UserRole;
import project.team.ondo.domain.user.constant.UserStatus;
import project.team.ondo.domain.user.entity.UserEntity;

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
        String profileImageKey,
        String bio,
        UserRole role,
        UserStatus status,
        double ratingAverage,
        long ratingCount
) {
    public static MyProfileResponse from(UserEntity user) {
        return new MyProfileResponse(
                user.getPublicId(),
                user.getLoginId(),
                user.getEmail(),
                user.getDisplayName(),
                user.getGender(),
                user.getMajor(),
                user.getInterests(),
                user.getProfileImageKey(),
                user.getBio(),
                user.getRole(),
                user.getStatus(),
                user.getRatingAvg(),
                user.getRatingCount()
        );
    }
}
