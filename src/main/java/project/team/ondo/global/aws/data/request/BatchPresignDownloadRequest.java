package project.team.ondo.global.aws.data.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record BatchPresignDownloadRequest(
        @NotNull
        @NotEmpty
        @Size(max = 50, message = "최대 50개의 키를 요청할 수 있습니다.")
        List<@NotNull @Size(min = 1, max = 1024) String> keys
) {
}
