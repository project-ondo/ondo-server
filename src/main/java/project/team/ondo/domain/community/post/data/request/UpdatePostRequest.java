package project.team.ondo.domain.community.post.data.request;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record UpdatePostRequest(
        @NotBlank String title,
        @NotBlank String content,
        List<String> tags
) {
}
