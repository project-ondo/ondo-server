package project.team.ondo.domain.rating.data.response;

import java.time.LocalDateTime;

public record UserRatingResponse(
        Long id,
        int stars,
        String comment,
        LocalDateTime createdAt
) {}
