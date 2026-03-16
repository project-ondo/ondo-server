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
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.global.controller.BaseApiController;
import project.team.ondo.global.response.ApiResponse;
import project.team.ondo.global.response.CursorResponse;
import project.team.ondo.global.response.PageResponse;
import project.team.ondo.global.security.annotation.CurrentUser;

import java.util.UUID;

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

    @PostMapping("/rooms")
    public ResponseEntity<@NonNull ApiResponse<CreateRoomResponse>> createRoom(
            @CurrentUser UserEntity me,
            @Valid @RequestBody CreateRoomRequest createRoomRequest
    ) {
        UUID chatRoomId = createRoomService.execute(me, createRoomRequest.targetUserPublicId());
        return ok("채팅방이 성공적으로 생성되었습니다.", new CreateRoomResponse(chatRoomId));
    }

    @GetMapping("/rooms")
    public ResponseEntity<@NonNull ApiResponse<PageResponse<ChatRoomListItemResponse>>> getRoomsList(
            @CurrentUser UserEntity me,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<@NonNull ChatRoomListItemResponse> result = getMyRoomsService.execute(me, pageable);
        return ok("채팅방 목록 조회에 성공했습니다.", PageResponse.from(result));
    }

    @GetMapping("/rooms/{chatRoomPublicId}/messages")
    public ResponseEntity<@NonNull ApiResponse<CursorResponse<ChatMessageResponse>>> getMessageList(
            @CurrentUser UserEntity me,
            @PathVariable UUID chatRoomPublicId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "30") int size
    ) {
        return ok("채팅 메시지 목록 조회에 성공했습니다.", getRoomMessagesService.execute(me, chatRoomPublicId, cursor, size));
    }

    @PatchMapping("/rooms/{chatRoomPublicId}/read")
    public ResponseEntity<@NonNull ApiResponse<Void>> read(
            @CurrentUser UserEntity me,
            @PathVariable UUID chatRoomPublicId,
            @RequestParam Long lastReadMessageId
    ) {
        markRoomReadService.execute(me, chatRoomPublicId, lastReadMessageId);
        return ok("채팅방 읽음 처리에 성공했습니다.");
    }

    @DeleteMapping("/rooms/{chatRoomPublicId}")
    public ResponseEntity<@NonNull ApiResponse<Void>> leaveRoom(
            @CurrentUser UserEntity me,
            @PathVariable UUID chatRoomPublicId
    ) {
        leaveRoomService.execute(me, chatRoomPublicId);
        return ok("채팅방 나가기에 성공했습니다.");
    }

    @PutMapping("/rooms/{chatRoomPublicId}/block")
    public ResponseEntity<@NonNull ApiResponse<Void>> blockRoom(
            @CurrentUser UserEntity me,
            @PathVariable UUID chatRoomPublicId
    ) {
        blockRoomService.execute(me, chatRoomPublicId);
        return ok("채팅방 차단에 성공했습니다.");
    }

    @DeleteMapping("/rooms/{chatRoomPublicId}/block")
    public ResponseEntity<@NonNull ApiResponse<Void>> unblockRoom(
            @CurrentUser UserEntity me,
            @PathVariable UUID chatRoomPublicId
    ) {
        unblockRoomService.execute(me, chatRoomPublicId);
        return ok("채팅방 차단 해제에 성공했습니다.");
    }

    @PutMapping("/rooms/{chatRoomPublicId}/mute")
    public ResponseEntity<@NonNull ApiResponse<Void>> muteRoom(
            @CurrentUser UserEntity me,
            @PathVariable UUID chatRoomPublicId
    ) {
        muteChatRoomService.execute(me, chatRoomPublicId);
        return ok("채팅방 알림 끄기에 성공했습니다.");
    }

    @DeleteMapping("/rooms/{chatRoomPublicId}/mute")
    public ResponseEntity<@NonNull ApiResponse<Void>> unmuteRoom(
            @CurrentUser UserEntity me,
            @PathVariable UUID chatRoomPublicId
    ) {
        unmuteChatRoomService.execute(me, chatRoomPublicId);
        return ok("채팅방 알림 켜기에 성공했습니다.");
    }
}
