package project.team.ondo.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.team.ondo.domain.user.data.request.UpdateMyProfileImageRequest;
import project.team.ondo.domain.user.data.request.UpdateMyProfileRequest;
import project.team.ondo.domain.user.data.request.UserSearchCondition;
import project.team.ondo.domain.user.data.response.MyProfileResponse;
import project.team.ondo.domain.user.data.response.UserPublicProfileResponse;
import project.team.ondo.domain.user.data.response.UserRecommendItemResponse;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.service.*;
import project.team.ondo.global.controller.BaseApiController;
import project.team.ondo.global.response.ApiResponse;
import project.team.ondo.global.response.PageResponse;
import project.team.ondo.global.security.annotation.CurrentUser;

import java.util.List;
import java.util.UUID;

@Tag(name = "User", description = "유저 프로필 및 추천")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController extends BaseApiController {

    private final GetMyProfileService getMyProfileService;
    private final GetUserPublicProfileService getUserPublicProfileService;
    private final UpdateMyProfileService updateMyProfileService;
    private final UserWithdrawService userWithdrawService;
    private final SearchUserService searchUserService;
    private final RecommendUserService recommendUserService;
    private final UpdateMyProfileImageService updateMyProfileImageService;

    @Operation(summary = "내 프로필 조회")
    @GetMapping("/my/profile")
    public ResponseEntity<@NonNull ApiResponse<MyProfileResponse>> getMyProfile(@CurrentUser UserEntity me) {
        return ok("내 프로필 정보 조회에 성공했습니다.", getMyProfileService.execute(me));
    }

    @Operation(summary = "내 프로필 수정")
    @PatchMapping("/my/profile")
    public ResponseEntity<@NonNull ApiResponse<Void>> updateMyProfile(
            @CurrentUser UserEntity me,
            @Valid @RequestBody UpdateMyProfileRequest request
    ) {
        updateMyProfileService.execute(me, request);
        return ok("내 프로필 정보 수정에 성공했습니다.");
    }

    @Operation(summary = "유저 공개 프로필 조회")
    @GetMapping("/{publicId}/profile")
    public ResponseEntity<@NonNull ApiResponse<UserPublicProfileResponse>> getUserPublicProfile(
            @Parameter(description = "조회할 유저의 publicId (UUID)") @PathVariable UUID publicId
    ) {
        return ok("유저 프로필 조회에 성공했습니다.", getUserPublicProfileService.execute(publicId));
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/my")
    public ResponseEntity<@NonNull ApiResponse<Void>> withdraw(@CurrentUser UserEntity me) {
        userWithdrawService.execute(me);
        return ok("회원 탈퇴가 성공적으로 완료되었습니다.");
    }

    @Operation(summary = "유저 검색")
    @GetMapping("/search")
    public ResponseEntity<@NonNull ApiResponse<PageResponse<@NonNull UserRecommendItemResponse>>> searchUsers(
            @Parameter(description = "검색 키워드") @RequestParam(required = false) String keyword,
            @Parameter(description = "전공") @RequestParam(required = false) String major,
            @Parameter(description = "관심사 목록") @RequestParam(required = false) List<String> interests,
            @Parameter(description = "정렬 기준 (기본값: 빈 문자열)") @RequestParam(required = false, defaultValue = "") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        UserSearchCondition condition = new UserSearchCondition(keyword, major, interests, sort);
        return ok("유저 검색에 성공했습니다.", PageResponse.from(searchUserService.execute(condition, pageable)));
    }

    @Operation(summary = "추천 유저 목록 조회")
    @GetMapping("/recommend")
    public ResponseEntity<@NonNull ApiResponse<PageResponse<UserRecommendItemResponse>>> recommendUsers(
            @CurrentUser UserEntity me,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ok("추천유저 조회에 성공했습니다.", PageResponse.from(recommendUserService.execute(me, pageable)));
    }

    @Operation(summary = "내 프로필 이미지 변경")
    @PutMapping("/my/profile/image")
    public ResponseEntity<@NonNull ApiResponse<Void>> updateMyProfileImage(
            @CurrentUser UserEntity me,
            @Valid @RequestBody UpdateMyProfileImageRequest request
    ) {
        updateMyProfileImageService.execute(me, request);
        return ok("프로필 이미지가 성공적으로 변경되었습니다.");
    }
}
