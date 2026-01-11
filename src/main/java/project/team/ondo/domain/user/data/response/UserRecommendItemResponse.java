package project.team.ondo.domain.user.data.response;

import project.team.ondo.domain.user.constant.Gender;

import java.util.List;
import java.util.UUID;

public record UserRecommendItemResponse(
        UUID publicId,
        String displayName,
        Gender gender,
        String major,
        List<String> interests,
        String profileImageUrl
) {
}
