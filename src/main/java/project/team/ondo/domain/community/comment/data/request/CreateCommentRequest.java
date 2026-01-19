package project.team.ondo.domain.community.comment.data.request;

import jakarta.validation.constraints.NotBlank;

public record CreateCommentRequest(
        @NotBlank String content
) {
}
