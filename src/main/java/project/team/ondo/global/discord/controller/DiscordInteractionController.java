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

    private static final int TYPE_PING      = 1;
    private static final int TYPE_COMPONENT = 3;

    private final DiscordSignatureVerifier signatureVerifier;
    private final DiscordWebhookService discordWebhookService;
    private final ApproveReportService approveReportService;
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
            String customId       = body.path("data").path("custom_id").asText();
            String token          = body.path("token").asText();
            String applicationId  = body.path("application_id").asText();
            return ResponseEntity.ok(handleButton(customId, token, applicationId));
        }

        return ResponseEntity.ok(Map.of("type", 1));
    }

    private Map<String, Object> handleButton(String customId, String token, String applicationId) {
        String[] parts = customId.split(":", 2);
        if (parts.length != 2) return errorResponse("잘못된 요청입니다.");

        String action   = parts[0];
        long   reportId;
        try {
            reportId = Long.parseLong(parts[1]);
        } catch (NumberFormatException e) {
            return errorResponse("잘못된 신고 ID입니다.");
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
        data.put("flags", 64); // EPHEMERAL: 클릭한 사람에게만 표시

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("type", 4);
        response.put("data", data);
        return response;
    }
}