package project.team.ondo.domain.user.controller;

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

    @GetMapping("/my/profile")
    public ResponseEntity<@NonNull ApiResponse<MyProfileResponse>> getMyProfile(@CurrentUser UserEntity me) {
        return ok("내 프로필 정보 조회에 성공했습니다.", getMyProfileService.execute(me));
    }

    @PatchMapping("/my/profile")
    public ResponseEntity<@NonNull ApiResponse<Void>> updateMyProfile(
            @CurrentUser UserEntity me,
            @Valid @RequestBody UpdateMyProfileRequest request
    ) {
        updateMyProfileService.execute(me, request);
        return ok("내 프로필 정보 수정에 성공했습니다.");
    }

    @GetMapping("/{publicId}/profile")
    public ResponseEntity<@NonNull ApiResponse<UserPublicProfileResponse>> getUserPublicProfile(@PathVariable UUID publicId) {
        return ok("유저 프로필 조회에 성공했습니다.", getUserPublicProfileService.execute(publicId));
    }

    @DeleteMapping("/my")
    public ResponseEntity<@NonNull ApiResponse<Void>> withdraw(@CurrentUser UserEntity me) {
        userWithdrawService.execute(me);
        return ok("회원 탈퇴가 성공적으로 완료되었습니다.");
    }

    @GetMapping("/search")
    public ResponseEntity<@NonNull ApiResponse<PageResponse<@NonNull UserRecommendItemResponse>>> searchUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String major,
            @RequestParam(required = false) List<String> interests,
            @RequestParam(required = false, defaultValue = "") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        UserSearchCondition condition = new UserSearchCondition(keyword, major, interests, sort);
        return ok("유저 검색에 성공했습니다.", PageResponse.from(searchUserService.execute(condition, pageable)));
    }

    @GetMapping("/recommend")
    public ResponseEntity<@NonNull ApiResponse<PageResponse<UserRecommendItemResponse>>> recommendUsers(
            @CurrentUser UserEntity me,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ok("추천유저 조회에 성공했습니다.", PageResponse.from(recommendUserService.execute(me, pageable)));
    }

    @PutMapping("/my/profile/image")
    public ResponseEntity<@NonNull ApiResponse<Void>> updateMyProfileImage(
            @CurrentUser UserEntity me,
            @Valid @RequestBody UpdateMyProfileImageRequest request
    ) {
        updateMyProfileImageService.execute(me, request);
        return ok("프로필 이미지가 성공적으로 변경되었습니다.");
    }
}