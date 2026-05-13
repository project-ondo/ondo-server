package project.team.ondo.domain.rating.data.request;

import jakarta.validation.constraints.*;

import java.util.List;

public record RateUserRequest(
        @NotNull @Min(1) @Max(5) Integer stars,
        @Size(max = 500) String comment,
        @NotEmpty @Size(max = 3) List<@NotBlank @Size(max = 20) String> tags
) {
}
