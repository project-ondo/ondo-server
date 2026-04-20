package project.team.ondo.domain.community.post.data.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SearchPostRequest(
        @NotBlank String keyword,
        String tag,
        @Pattern(regexp = "latest|popular", flags = Pattern.Flag.CASE_INSENSITIVE) String sort
) {
    public boolean isLatest() {
        return sort == null || sort.isBlank() || sort.equalsIgnoreCase("latest");
    }
}
