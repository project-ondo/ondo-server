package project.team.ondo.domain.chat.controller;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.team.ondo.domain.chat.data.request.CreateRoomRequest;
import project.team.ondo.domain.chat.data.response.ChatMessageResponse;
import project.team.ondo.domain.chat.data.response.ChatRoomListItemResponse;
import project.team.ondo.domain.chat.data.response.CreateRoomResponse;
import project.team.ondo.domain.chat.service.*;
import project.team.ondo.global.response.ApiResponse;
import project.team.ondo.global.response.PageResponse;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final CreateRoomService createRoomService;
    private final GetMyRoomsService getMyRoomsService;
    private final GetRoomMessagesService getRoomMessagesService;
    private final MarkRoomReadService markRoomReadService;
    private final LeaveRoomService leaveRoomService;
    private final BlockRoomService blockRoomService;
    private final UnblockRoomService unblockRoomService;

    @PostMapping("/rooms")
    public ResponseEntity<@NonNull ApiResponse<CreateRoomResponse>> createRoom(@Valid @RequestBody CreateRoomRequest createRoomRequest) {
        UUID chatRoomId = createRoomService.execute(createRoomRequest.targetUserPublicId());
        return ResponseEntity.ok(
                ApiResponse.success(
                        "채팅방이 성공적으로 생성되었습니다.",
                        new CreateRoomResponse(chatRoomId)
                )
        );
    }

    @GetMapping("/rooms")
    public ResponseEntity<@NonNull ApiResponse<PageResponse<ChatRoomListItemResponse>>> getRoomsList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<@NonNull ChatRoomListItemResponse> result = getMyRoomsService.execute(pageable);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "채팅방 목록 조회에 성공했습니다.",
                        PageResponse.from(result)
                )
        );
    }

    @GetMapping("/rooms/{chatRoomId}/messages")
    public ResponseEntity<@NonNull ApiResponse<PageResponse<ChatMessageResponse>>> getMessagesList(
            @PathVariable UUID chatRoomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<@NonNull ChatMessageResponse> result = getRoomMessagesService.execute(chatRoomId, pageable);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "메시지 조회에 성공했습니다.",
                        PageResponse.from(result)
                )
        );
    }

    @PutMapping("/rooms/{chatRoomId}/read")
    public ResponseEntity<@NonNull ApiResponse<Void>> read(
        @PathVariable UUID chatRoomId,
        @RequestParam Long lastReadMessageId
    ) {
        markRoomReadService.execute(chatRoomId, lastReadMessageId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "채팅방 읽음 처리에 성공했습니다."
                )
        );
    }

    @DeleteMapping("/rooms/{chatRoomId}")
    public ResponseEntity<@NonNull ApiResponse<Void>> leaveRoom(@PathVariable UUID chatRoomId) {
        leaveRoomService.execute(chatRoomId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "채팅방 나가기에 성공했습니다."
                )
        );
    }

    @PutMapping("/rooms/{chatRoomId}/block")
    public ResponseEntity<@NonNull ApiResponse<Void>> blockRoom(@PathVariable UUID chatRoomId) {
        blockRoomService.execute(chatRoomId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "채팅방 차단에 성공했습니다."
                )
        );
    }

    @DeleteMapping("/rooms/{chatRoomId}/block")
    public ResponseEntity<@NonNull ApiResponse<Void>> unblockRoom(@PathVariable UUID chatRoomId) {
        unblockRoomService.execute(chatRoomId);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "채팅방 차단 해제에 성공했습니다."
                )
        );
    }
}
