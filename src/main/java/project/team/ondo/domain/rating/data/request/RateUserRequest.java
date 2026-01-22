package project.team.ondo.domain.rating.data.request;

import jakarta.validation.constraints.NotNull;

public record RateUserRequest(
        @NotNull Integer stars,
        String comment
) {
}
