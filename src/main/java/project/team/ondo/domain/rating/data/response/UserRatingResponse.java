package project.team.ondo.domain.rating.data.response;

import project.team.ondo.domain.rating.entity.UserRatingEntity;

import java.time.LocalDateTime;
import java.util.List;

public record UserRatingResponse(
        Long id,
        int stars,
        String comment,
        List<String> tags,
        LocalDateTime createdAt
) {
    public static UserRatingResponse from(UserRatingEntity entity) {
        return new UserRatingResponse(
                entity.getId(),
                entity.getStars(),
                entity.getComment(),
                entity.getTags(),
                entity.getCreatedAt()
        );
    }
}
