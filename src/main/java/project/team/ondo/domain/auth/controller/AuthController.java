package project.team.ondo.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.team.ondo.domain.auth.data.request.*;
import project.team.ondo.domain.auth.data.response.AuthTokenResponse;
import project.team.ondo.domain.auth.data.response.VerificationTokenResponse;
import project.team.ondo.domain.auth.service.*;
import project.team.ondo.global.controller.BaseApiController;
import project.team.ondo.global.response.ApiResponse;

@Tag(name = "Auth", description = "인증 (회원가입, 로그인, 토큰)")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController extends BaseApiController {

    private final SendAuthCodeService sendAuthCodeService;
    private final VerifyAuthCodeService verifyAuthCodeService;
    private final SignUpService signUpService;
    private final SignInService signInService;
    private final RefreshService refreshService;
    private final LogoutService logoutService;

    @Operation(summary = "이메일 인증코드 발송")
    @PostMapping("/email/send")
    public ResponseEntity<@NonNull ApiResponse<Void>> sendAuthCode(@Valid @RequestBody SendEmailRequest request) {
        sendAuthCodeService.execute(request.email());
        return ok("인증코드가 성공적으로 발송되었습니다.");
    }

    @Operation(summary = "이메일 인증코드 검증 및 인증 토큰 발급")
    @PostMapping("/email/verify")
    public ResponseEntity<@NonNull ApiResponse<VerificationTokenResponse>> verifyAuthCode(@Valid @RequestBody VerificationCodeRequest request) {
        String token = verifyAuthCodeService.execute(request.email(), request.code());
        return ok("이메일 인증이 성공적으로 완료되었습니다.", new VerificationTokenResponse(token));
    }

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<@NonNull ApiResponse<Void>> signUp(@Valid @RequestBody SignUpRequest request) {
        signUpService.execute(request);
        return ok("회원가입이 성공적으로 완료되었습니다.");
    }

    @Operation(summary = "로그인")
    @PostMapping("/signin")
    public ResponseEntity<@NonNull ApiResponse<AuthTokenResponse>> signIn(@Valid @RequestBody SignInRequest request) {
        return ok("로그인이 성공적으로 완료되었습니다.", signInService.execute(request));
    }

    @Operation(summary = "액세스 토큰 재발급")
    @PostMapping("/refresh")
    public ResponseEntity<@NonNull ApiResponse<AuthTokenResponse>> refresh(@Valid @RequestBody RefreshRequest request) {
        return ok("토큰이 성공적으로 재발급되었습니다.", refreshService.execute(request));
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<@NonNull ApiResponse<Void>> logout(@Valid @RequestBody LogoutRequest request) {
        logoutService.execute(request);
        return ok("로그아웃이 성공적으로 완료되었습니다.");
    }
}