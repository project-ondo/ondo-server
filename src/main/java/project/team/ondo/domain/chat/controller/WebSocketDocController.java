package project.team.ondo.domain.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import project.team.ondo.domain.chat.data.payload.*;
import project.team.ondo.domain.chat.data.request.*;
import project.team.ondo.domain.chat.data.response.ChatMessageResponse;

import java.util.UUID;

/**
 * WebSocket(STOMP) API 문서 전용 컨트롤러. 실제로 호출되지 않습니다.
 *
 * 연결: ws://{host}/ws
 * 인증: STOMP CONNECT 헤더에 Authorization: Bearer {JWT} 포함
 * PUBLISH prefix: /pub
 * SUBSCRIBE prefix: /topic, /user
 */
@Tag(
        name = "WebSocket",
        description = """
                **STOMP WebSocket API**

                - 연결 엔드포인트: `ws://{host}/ws`
                - 인증: STOMP CONNECT 프레임 헤더에 `Authorization: Bearer {JWT}` 포함
                - PUBLISH prefix: `/pub`
                - SUBSCRIBE prefix: `/topic`, `/user`

                [PUBLISH] 항목은 클라이언트가 서버로 전송하는 메시지이고,
                [SUBSCRIBE] 항목은 서버에서 클라이언트로 수신되는 메시지입니다.
                """
)
@RestController
@RequestMapping("/ws-docs")
public class WebSocketDocController {

    // ─────────────────────────────────────────────
    // PUBLISH  (클라이언트 → 서버)
    // ─────────────────────────────────────────────

    @Operation(
            summary = "[PUBLISH] 채팅 메시지 전송",
            description = "**STOMP Destination:** `SEND /pub/chat.send`\n\n채팅방에 텍스트 또는 이미지 메시지를 전송합니다."
    )
    @PostMapping("/chat/send")
    public void sendMessage(@RequestBody SendMessageRequest request) {}

    @Operation(
            summary = "[PUBLISH] 채팅방 읽음 처리",
            description = "**STOMP Destination:** `SEND /pub/chat.read`\n\n지정한 메시지까지 읽음 상태로 표시합니다."
    )
    @PostMapping("/chat/read")
    public void markRead(@RequestBody MarkReadWsRequest request) {}

    @Operation(
            summary = "[PUBLISH] 타이핑 상태 전송",
            description = "**STOMP Destination:** `SEND /pub/chat.typing`\n\n타이핑 중 여부를 서버로 전송합니다. 스팸 방지를 위해 1초 단위로 throttle됩니다."
    )
    @PostMapping("/chat/typing")
    public void typing(@RequestBody TypingWsRequest request) {}

    @Operation(
            summary = "[PUBLISH] 채팅방 입장",
            description = "**STOMP Destination:** `SEND /pub/chat.room.enter`\n\n채팅방에 입장했음을 서버에 알립니다. 다른 참여자에게 presence 이벤트가 전파됩니다."
    )
    @PostMapping("/chat/room/enter")
    public void enterRoom(@RequestBody EnterRoomRequest request) {}

    @Operation(
            summary = "[PUBLISH] 채팅방 퇴장",
            description = "**STOMP Destination:** `SEND /pub/chat.room.leave`\n\n채팅방에서 퇴장했음을 서버에 알립니다."
    )
    @PostMapping("/chat/room/leave")
    public void leaveRoom(@RequestBody LeaveRoomRequest request) {}

    @Operation(
            summary = "[PUBLISH] 연결 유지 (ping)",
            description = "**STOMP Destination:** `SEND /pub/chat.ping`\n\nbody 없이 전송합니다. 서버에 온라인 상태를 갱신합니다 (Redis TTL 90초 연장)."
    )
    @PostMapping("/chat/ping")
    public void ping() {}

    // ─────────────────────────────────────────────
    // SUBSCRIBE  (서버 → 클라이언트)
    // ─────────────────────────────────────────────

    @Operation(
            summary = "[SUBSCRIBE] 채팅 메시지 수신",
            description = "**STOMP Destination:** `SUBSCRIBE /topic/chat.rooms.{roomPublicId}`\n\n채팅방의 새 메시지를 실시간으로 수신합니다."
    )
    @GetMapping("/topic/chat/rooms/{roomPublicId}/messages")
    public ChatMessageResponse subscribeMessages(@PathVariable UUID roomPublicId) { return null; }

    @Operation(
            summary = "[SUBSCRIBE] 읽음 상태 수신",
            description = "**STOMP Destination:** `SUBSCRIBE /topic/chat.rooms.{roomPublicId}.read`\n\n다른 참여자가 메시지를 읽었을 때 읽음 이벤트를 수신합니다."
    )
    @GetMapping("/topic/chat/rooms/{roomPublicId}/read")
    public ChatReadEventPayload subscribeRead(@PathVariable UUID roomPublicId) { return null; }

    @Operation(
            summary = "[SUBSCRIBE] 타이핑 상태 수신",
            description = "**STOMP Destination:** `SUBSCRIBE /topic/chat.rooms.{roomPublicId}.typing`\n\n다른 참여자의 타이핑 여부를 실시간으로 수신합니다."
    )
    @GetMapping("/topic/chat/rooms/{roomPublicId}/typing")
    public ChatTypingEventPayload subscribeTyping(@PathVariable UUID roomPublicId) { return null; }

    @Operation(
            summary = "[SUBSCRIBE] 사용자 presence 수신",
            description = "**STOMP Destination:** `SUBSCRIBE /topic/chat.rooms.{roomPublicId}.presence`\n\n채팅방 내 다른 참여자의 온라인/오프라인 상태 변화를 수신합니다."
    )
    @GetMapping("/topic/chat/rooms/{roomPublicId}/presence")
    public ChatPresencePayload subscribePresence(@PathVariable UUID roomPublicId) { return null; }

    @Operation(
            summary = "[SUBSCRIBE] 채팅방 목록 업데이트 수신",
            description = "**STOMP Destination:** `SUBSCRIBE /user/{userId}/queue/chat.rooms.update`\n\n본인의 채팅방 목록에서 안읽은 메시지 수, 마지막 메시지 미리보기 등이 변경될 때 수신합니다."
    )
    @GetMapping("/user/queue/chat/rooms/update")
    public ChatRoomListUpdatePayload subscribeRoomUpdate() { return null; }
}
