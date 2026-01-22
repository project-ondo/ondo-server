package project.team.ondo.global.aws.controller;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.team.ondo.global.aws.data.request.PresignUploadRequest;
import project.team.ondo.global.aws.data.response.PresignDownloadResponse;
import project.team.ondo.global.aws.data.response.PresignUploadResponse;
import project.team.ondo.global.aws.service.MediaPresignService;
import project.team.ondo.global.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/media/presign")
public class MediaPresignController {

    private final MediaPresignService mediaPresignService;

    @PostMapping("/upload")
    public ResponseEntity<@NonNull ApiResponse<PresignUploadResponse>> presignUpload(
            @Valid @RequestBody PresignUploadRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "업로드용 Presigned URL 생성에 성공했습니다.",
                        mediaPresignService.presignUpload(request)
                )
        );
    }

    @GetMapping("/download")
    public ResponseEntity<@NonNull ApiResponse<PresignDownloadResponse>> presignDownload(
            @RequestParam String key
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "다운로드용 Presigned URL 생성에 성공했습니다.",
                        mediaPresignService.presignDownload(key)
                )
        );
    }
}
