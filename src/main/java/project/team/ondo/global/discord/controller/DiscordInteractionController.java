package project.team.ondo.global.discord.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.team.ondo.domain.report.exception.ReportAlreadyProcessedException;
import project.team.ondo.domain.report.exception.ReportNotFoundException;
import project.team.ondo.domain.report.service.ApproveReportService;
import project.team.ondo.domain.report.service.ApproveUserReportService;
import project.team.ondo.domain.report.service.RejectReportService;
import project.team.ondo.global.discord.DiscordSignatureVerifier;
import project.team.ondo.global.discord.DiscordWebhookService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/discord")
public class DiscordInteractionController {

    private static final int TYPE_PING         = 1;
    private static final int TYPE_COMPONENT    = 3;
    private static final int TYPE_MODAL_SUBMIT = 5;

    private final DiscordSignatureVerifier signatureVerifier;
    private final DiscordWebhookService discordWebhookService;
    private final ApproveReportService approveReportService;
    private final ApproveUserReportService approveUserReportService;
    private final RejectReportService rejectReportService;
    private final ObjectMapper objectMapper;

    @PostMapping("/interactions")
    public ResponseEntity<Map<String, Object>> handleInteraction(
            HttpServletRequest httpRequest,
            @RequestHeader("X-Signature-Ed25519") String signature,
            @RequestHeader("X-Signature-Timestamp") String timestamp
    ) throws Exception {
        byte[] bodyBytes = httpRequest.getInputStream().readAllBytes();

        if (!signatureVerifier.verify(signature, timestamp, bodyBytes)) {
            return ResponseEntity.status(401).build();
        }

        JsonNode body = objectMapper.readTree(bodyBytes);
        int type = body.get("type").asInt();

        if (type == TYPE_PING) {
            return ResponseEntity.ok(Map.of("type", 1));
        }

        if (type == TYPE_COMPONENT) {
            String customId      = body.path("data").path("custom_id").asText();
            String token         = body.path("token").asText();
            String applicationId = body.path("application_id").asText();
            return ResponseEntity.ok(handleButton(customId, token, applicationId));
        }

        if (type == TYPE_MODAL_SUBMIT) {
            String customId = body.path("data").path("custom_id").asText();
            String token    = body.path("token").asText();
            String appId    = body.path("application_id").asText();
            return ResponseEntity.ok(handleModalSubmit(customId, token, appId, body));
        }

        return ResponseEntity.ok(Map.of("type", 1));
    }

    private Map<String, Object> handleButton(String customId, String token, String applicationId) {
        String[] parts = customId.split(":", 2);
        if (parts.length != 2) return errorResponse("잘못된 요청입니다.");

        String action = parts[0];
        long reportId;
        try {
            reportId = Long.parseLong(parts[1]);
        } catch (NumberFormatException e) {
            return errorResponse("잘못된 신고 ID입니다.");
        }

        if ("approve_user".equals(action)) {
            return suspensionModal(reportId);
        }

        try {
            return switch (action) {
                case "approve" -> {
                    approveReportService.execute(reportId);
                    discordWebhookService.removeButtons(applicationId, token);
                    yield resultMessage("✅ 신고 승인됨", "콘텐츠가 삭제되고 신고자에게 결과가 통보되었습니다.", 5763719);
                }
                case "reject" -> {
                    rejectReportService.execute(reportId);
                    discordWebhookService.removeButtons(applicationId, token);
                    yield resultMessage("❌ 신고 기각됨", "신고자에게 기각 사실이 통보되었습니다.", 10197915);
                }
                default -> errorResponse("알 수 없는 액션입니다.");
            };
        } catch (ReportNotFoundException | ReportAlreadyProcessedException e) {
            return errorResponse("이미 처리되었거나 존재하지 않는 신고입니다.");
        } catch (Exception e) {
            log.error("Failed to process Discord interaction action={} reportId={}", action, reportId, e);
            return errorResponse("처리 중 오류가 발생했습니다.");
        }
    }

    private Map<String, Object> handleModalSubmit(String customId, String token, String applicationId, JsonNode body) {
        String[] parts = customId.split(":", 2);
        if (parts.length != 2 || !"suspend".equals(parts[0])) return errorResponse("잘못된 모달 요청입니다.");

        long reportId;
        try {
            reportId = Long.parseLong(parts[1]);
        } catch (NumberFormatException e) {
            return errorResponse("잘못된 신고 ID입니다.");
        }

        String daysValue = body.path("data").path("components")
                .path(0).path("components").path(0).path("value").asText("");
        int suspensionDays;
        try {
            suspensionDays = Integer.parseInt(daysValue.trim());
            if (suspensionDays <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            return errorResponse("유효한 정지 기간(일)을 입력해주세요.");
        }

        try {
            approveUserReportService.execute(reportId, suspensionDays);
            discordWebhookService.removeButtons(applicationId, token);
            return resultMessage("✅ 유저 신고 승인됨",
                    suspensionDays + "일 계정 정지가 적용되었습니다. 신고자에게 결과가 통보되었습니다.", 5763719);
        } catch (ReportNotFoundException | ReportAlreadyProcessedException e) {
            return errorResponse("이미 처리되었거나 존재하지 않는 신고입니다.");
        } catch (Exception e) {
            log.error("Failed to process user suspension reportId={} days={}", reportId, suspensionDays, e);
            return errorResponse("처리 중 오류가 발생했습니다.");
        }
    }

    private Map<String, Object> suspensionModal(long reportId) {
        Map<String, Object> textInput = new LinkedHashMap<>();
        textInput.put("type", 4);
        textInput.put("custom_id", "days");
        textInput.put("label", "정지 기간 (일)");
        textInput.put("style", 1);
        textInput.put("min_length", 1);
        textInput.put("max_length", 3);
        textInput.put("required", true);
        textInput.put("placeholder", "예: 7");

        Map<String, Object> actionRow = new LinkedHashMap<>();
        actionRow.put("type", 1);
        actionRow.put("components", List.of(textInput));

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("custom_id", "suspend:" + reportId);
        data.put("title", "계정 정지 기간 설정");
        data.put("components", List.of(actionRow));

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("type", 9);
        response.put("data", data);
        return response;
    }

    private Map<String, Object> resultMessage(String title, String description, int color) {
        Map<String, Object> embed = new LinkedHashMap<>();
        embed.put("title", title);
        embed.put("description", description);
        embed.put("color", color);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("embeds", List.of(embed));

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("type", 4);
        response.put("data", data);
        return response;
    }

    private Map<String, Object> errorResponse(String message) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("content", "⚠️ " + message);
        data.put("flags", 64);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("type", 4);
        response.put("data", data);
        return response;
    }
}
