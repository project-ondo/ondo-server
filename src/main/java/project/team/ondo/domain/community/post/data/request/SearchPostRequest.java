package project.team.ondo.domain.community.post.data.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record SearchPostRequest(
        @NotBlank String keyword,
        @Size(max = 10) List<String> tags,
        @Pattern(regexp = "latest|popular", flags = Pattern.Flag.CASE_INSENSITIVE) String sort
) {
    public boolean isLatest() {
        return sort == null || sort.isBlank() || sort.equalsIgnoreCase("latest");
    }

    public List<String> normalizedTags() {
        if (tags == null) return List.of();
        return tags.stream()
                .filter(t -> t != null && !t.isBlank())
                .map(t -> t.trim().toLowerCase())
                .distinct()
                .toList();
    }
}
