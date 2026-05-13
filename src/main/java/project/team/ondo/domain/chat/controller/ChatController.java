package project.team.ondo.domain.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
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
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.global.controller.BaseApiController;
import project.team.ondo.global.response.ApiResponse;
import project.team.ondo.global.response.CursorResponse;
import project.team.ondo.global.response.PageResponse;
import project.team.ondo.global.security.annotation.CurrentUser;

import java.util.UUID;

@Tag(name = "Chat", description = "채팅방 관리")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController extends BaseApiController {

    private final CreateRoomService createRoomService;
    private final GetMyRoomsService getMyRoomsService;
    private final GetRoomMessagesService getRoomMessagesService;
    private final MarkRoomReadService markRoomReadService;
    private final LeaveRoomService leaveRoomService;
    private final BlockRoomService blockRoomService;
    private final UnblockRoomService unblockRoomService;
    private final MuteChatRoomService muteChatRoomService;
    private final UnmuteChatRoomService unmuteChatRoomService;

    @Operation(summary = "채팅방 생성")
    @PostMapping("/rooms")
    public ResponseEntity<@NonNull ApiResponse<CreateRoomResponse>> createRoom(
            @CurrentUser UserEntity me,
            @Valid @RequestBody CreateRoomRequest createRoomRequest
    ) {
        UUID chatRoomId = createRoomService.execute(me, createRoomRequest.targetUserPublicId());
        return ok("채팅방이 성공적으로 생성되었습니다.", new CreateRoomResponse(chatRoomId));
    }

    @Operation(summary = "내 채팅방 목록 조회")
    @GetMapping("/rooms")
    public ResponseEntity<@NonNull ApiResponse<PageResponse<ChatRoomListItemResponse>>> getRoomsList(
            @CurrentUser UserEntity me,
            @RequestParam(defaultValue = "0") int page,
            @Positive @Max(100) @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<@NonNull ChatRoomListItemResponse> result = getMyRoomsService.execute(me, pageable);
        return ok("채팅방 목록 조회에 성공했습니다.", PageResponse.from(result));
    }

    @Operation(summary = "채팅 메시지 목록 조회 (커서 기반)")
    @GetMapping("/rooms/{chatRoomPublicId}/messages")
    public ResponseEntity<@NonNull ApiResponse<CursorResponse<ChatMessageResponse>>> getMessageList(
            @CurrentUser UserEntity me,
            @Parameter(description = "채팅방 publicId (UUID)") @PathVariable UUID chatRoomPublicId,
            @Parameter(description = "커서 (마지막 조회 메시지 ID, 첫 조회 시 생략)") @RequestParam(required = false) Long cursor,
            @Positive @Max(100) @RequestParam(defaultValue = "30") int size
    ) {
        return ok("채팅 메시지 목록 조회에 성공했습니다.", getRoomMessagesService.execute(me, chatRoomPublicId, cursor, size));
    }

    @Operation(summary = "채팅방 읽음 처리")
    @PatchMapping("/rooms/{chatRoomPublicId}/read")
    public ResponseEntity<@NonNull ApiResponse<Void>> read(
            @CurrentUser UserEntity me,
            @Parameter(description = "채팅방 publicId (UUID)") @PathVariable UUID chatRoomPublicId,
            @Parameter(description = "마지막으로 읽은 메시지 ID") @RequestParam Long lastReadMessageId
    ) {
        markRoomReadService.execute(me, chatRoomPublicId, lastReadMessageId);
        return ok("채팅방 읽음 처리에 성공했습니다.");
    }

    @Operation(summary = "채팅방 나가기")
    @DeleteMapping("/rooms/{chatRoomPublicId}")
    public ResponseEntity<@NonNull ApiResponse<Void>> leaveRoom(
            @CurrentUser UserEntity me,
            @Parameter(description = "채팅방 publicId (UUID)") @PathVariable UUID chatRoomPublicId
    ) {
        leaveRoomService.execute(me, chatRoomPublicId);
        return ok("채팅방 나가기에 성공했습니다.");
    }

    @Operation(summary = "채팅방 차단")
    @PutMapping("/rooms/{chatRoomPublicId}/block")
    public ResponseEntity<@NonNull ApiResponse<Void>> blockRoom(
            @CurrentUser UserEntity me,
            @Parameter(description = "채팅방 publicId (UUID)") @PathVariable UUID chatRoomPublicId
    ) {
        blockRoomService.execute(me, chatRoomPublicId);
        return ok("채팅방 차단에 성공했습니다.");
    }

    @Operation(summary = "채팅방 차단 해제")
    @DeleteMapping("/rooms/{chatRoomPublicId}/block")
    public ResponseEntity<@NonNull ApiResponse<Void>> unblockRoom(
            @CurrentUser UserEntity me,
            @Parameter(description = "채팅방 publicId (UUID)") @PathVariable UUID chatRoomPublicId
    ) {
        unblockRoomService.execute(me, chatRoomPublicId);
        return ok("채팅방 차단 해제에 성공했습니다.");
    }

    @Operation(summary = "채팅방 알림 끄기")
    @PutMapping("/rooms/{chatRoomPublicId}/mute")
    public ResponseEntity<@NonNull ApiResponse<Void>> muteRoom(
            @CurrentUser UserEntity me,
            @Parameter(description = "채팅방 publicId (UUID)") @PathVariable UUID chatRoomPublicId
    ) {
        muteChatRoomService.execute(me, chatRoomPublicId);
        return ok("채팅방 알림 끄기에 성공했습니다.");
    }

    @Operation(summary = "채팅방 알림 켜기")
    @DeleteMapping("/rooms/{chatRoomPublicId}/mute")
    public ResponseEntity<@NonNull ApiResponse<Void>> unmuteRoom(
            @CurrentUser UserEntity me,
            @Parameter(description = "채팅방 publicId (UUID)") @PathVariable UUID chatRoomPublicId
    ) {
        unmuteChatRoomService.execute(me, chatRoomPublicId);
        return ok("채팅방 알림 켜기에 성공했습니다.");
    }
}