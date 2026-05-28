package project.team.ondo.domain.report.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.team.ondo.domain.report.data.request.CreateReportRequest;
import project.team.ondo.domain.report.service.CreateReportService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.global.controller.BaseApiController;
import project.team.ondo.global.response.ApiResponse;
import project.team.ondo.global.security.annotation.CurrentUser;

@Tag(name = "Report", description = "신고")
@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController extends BaseApiController {

    private final CreateReportService createReportService;

    @Operation(summary = "신고 접수")
    @PostMapping
    public ResponseEntity<@NonNull ApiResponse<Long>> createReport(
            @CurrentUser UserEntity me,
            @Valid @RequestBody CreateReportRequest request
    ) {
        return ok("신고가 접수되었습니다.", createReportService.execute(me, request));
    }
}
