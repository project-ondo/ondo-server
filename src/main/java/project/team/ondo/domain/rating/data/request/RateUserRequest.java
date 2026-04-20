package project.team.ondo.domain.rating.data.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RateUserRequest(
        @NotNull @Min(1) @Max(5) Integer stars,
        @Size(max = 500) String comment
) {
}
