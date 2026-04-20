package project.team.ondo.domain.community.post.data.request;

import jakarta.validation.constraints.NotBlank;

public record SearchPostRequest(
        @NotBlank String keyword,
        String tag,
        String sort
) {
    public boolean isLatest() {
        return sort == null || sort.isBlank() || sort.equalsIgnoreCase("latest");
    }
}
