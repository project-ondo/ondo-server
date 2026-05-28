package project.team.ondo.global.discord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import project.team.ondo.domain.report.event.ReportCreatedEvent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscordWebhookService {

    private static final String DISCORD_API = "https://discord.com/api/v10";

    @Value("${discord.bot-token:}")
    private String botToken;

    @Value("${discord.channel-id:}")
    private String channelId;

    private final RestClient restClient = RestClient.create();

    public void removeButtons(String applicationId, String interactionToken) {
        try {
            restClient.patch()
                    .uri(DISCORD_API + "/webhooks/{appId}/{token}/messages/@original", applicationId, interactionToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("components", List.of()))
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.warn("Failed to remove buttons from original Discord message appId={}", applicationId, e);
        }
    }

    public void sendReportNotification(ReportCreatedEvent event) {
        if (botToken.isBlank() || channelId.isBlank()) {
            log.warn("Discord bot-token or channel-id not configured, skipping report notification");
            return;
        }
        try {
            restClient.post()
                    .uri(DISCORD_API + "/channels/" + channelId + "/messages")
                    .header("Authorization", "Bot " + botToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(buildMessage(event))
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.error("Failed to send Discord bot message for reportId={}", event.reportId(), e);
        }
    }

    private Map<String, Object> buildMessage(ReportCreatedEvent event) {
        List<Map<String, Object>> fields = new ArrayList<>();
        fields.add(field("신고 ID", String.valueOf(event.reportId()), true));
        fields.add(field("대상 유형", event.targetType().name(), true));
        fields.add(field("대상 ID", String.valueOf(event.targetId()), true));
        fields.add(field("신고자", event.reporterPublicId().toString(), false));
        fields.add(field("신고 사유", truncate(event.description(), 1000), false));
        fields.add(field("콘텐츠 스냅샷", truncate(event.contentSnapshot(), 1000), false));

        Map<String, Object> embed = new LinkedHashMap<>();
        embed.put("title", "🚨 새 신고 접수");
        embed.put("color", 15548997);  // red
        embed.put("fields", fields);

        Map<String, Object> approveBtn = button(3, "✅ 승인 (콘텐츠 삭제)", "approve:" + event.reportId());
        Map<String, Object> rejectBtn  = button(4, "❌ 기각", "reject:" + event.reportId());

        Map<String, Object> actionRow = new LinkedHashMap<>();
        actionRow.put("type", 1);
        actionRow.put("components", List.of(approveBtn, rejectBtn));

        Map<String, Object> message = new LinkedHashMap<>();
        message.put("embeds", List.of(embed));
        message.put("components", List.of(actionRow));
        return message;
    }

    private Map<String, Object> button(int style, String label, String customId) {
        Map<String, Object> btn = new LinkedHashMap<>();
        btn.put("type", 2);
        btn.put("style", style);
        btn.put("label", label);
        btn.put("custom_id", customId);
        return btn;
    }

    private Map<String, Object> field(String name, String value, boolean inline) {
        Map<String, Object> f = new LinkedHashMap<>();
        f.put("name", name);
        f.put("value", value != null ? value : "(없음)");
        f.put("inline", inline);
        return f;
    }

    private String truncate(String value, int max) {
        if (value == null) return "(없음)";
        return value.length() > max ? value.substring(0, max) + "..." : value;
    }
}