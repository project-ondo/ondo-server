package project.team.ondo.global.aws.data.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import project.team.ondo.global.aws.constant.MediaCategory;

import java.util.UUID;

public record PresignUploadRequest(
        @NotNull MediaCategory category,
        @NotBlank String contentType,
        UUID chatRoomPublicId
) {
}
