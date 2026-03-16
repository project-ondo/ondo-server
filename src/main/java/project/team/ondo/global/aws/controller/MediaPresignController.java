package project.team.ondo.global.aws.controller;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.global.aws.data.request.BatchPresignDownloadRequest;
import project.team.ondo.global.aws.data.request.PresignUploadRequest;
import project.team.ondo.global.aws.data.response.BatchPresignDownloadResponse;
import project.team.ondo.global.aws.data.response.PresignDownloadResponse;
import project.team.ondo.global.aws.data.response.PresignUploadResponse;
import project.team.ondo.global.aws.service.MediaPresignService;
import project.team.ondo.global.controller.BaseApiController;
import project.team.ondo.global.response.ApiResponse;
import project.team.ondo.global.security.annotation.CurrentUser;

@RestController
@RequiredArgsConstructor
@RequestMapping("/media/presign")
public class MediaPresignController extends BaseApiController {

    private final MediaPresignService mediaPresignService;

    @PostMapping("/upload")
    public ResponseEntity<@NonNull ApiResponse<PresignUploadResponse>> presignUpload(
            @CurrentUser UserEntity me,
            @Valid @RequestBody PresignUploadRequest request
    ) {
        return ok("업로드용 Presigned URL 생성에 성공했습니다.", mediaPresignService.presignUpload(me, request));
    }

    @PostMapping("/download")
    public ResponseEntity<@NonNull ApiResponse<PresignDownloadResponse>> presignDownload(
            @CurrentUser UserEntity me,
            @RequestParam String key
    ) {
        return ok("다운로드용 Presigned URL 생성에 성공했습니다.", mediaPresignService.presignDownload(me, key));
    }

    @PostMapping("/download/batch")
    public ResponseEntity<@NonNull ApiResponse<BatchPresignDownloadResponse>> presignDownloadBatch(
            @CurrentUser UserEntity me,
            @Valid @RequestBody BatchPresignDownloadRequest request
    ) {
        return ok("다운로드용 Presigned URL(배치) 생성에 성공했습니다.", mediaPresignService.presignDownloadBatch(me, request));
    }
}
