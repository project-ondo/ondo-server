package project.team.ondo.domain.user.data.response;

import project.team.ondo.domain.user.constant.Gender;
import project.team.ondo.domain.user.entity.UserEntity;

import java.util.List;
import java.util.UUID;

public record UserRecommendItemResponse(
        UUID publicId,
        String displayName,
        Gender gender,
        String major,
        List<String> interests,
        String profileImageKey,
        double ratingAverage,
        long ratingCount
) {
    public static UserRecommendItemResponse from(UserEntity user) {
        return new UserRecommendItemResponse(
                user.getPublicId(),
                user.getDisplayName(),
                user.getGender(),
                user.getMajor(),
                user.getInterests(),
                user.getProfileImageKey(),
                user.getRatingAvg(),
                user.getRatingCount()
        );
    }
}
