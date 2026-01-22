package project.team.ondo.global.fcm.data.command;

import java.util.Map;
import java.util.UUID;

public record FcmPushCommand(
        UUID receiverUserPublicId,
        String title,
        String body,
        Map<String, String> data
) {
}
