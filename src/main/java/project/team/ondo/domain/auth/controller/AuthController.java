package project.team.ondo.domain.auth.controller;

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

    @PostMapping("/email/send")
    public ResponseEntity<@NonNull ApiResponse<Void>> sendAuthCode(@Valid @RequestBody SendEmailRequest request) {
        sendAuthCodeService.execute(request.email());
        return ok("인증코드가 성공적으로 발송되었습니다.");
    }

    @PostMapping("/email/verify")
    public ResponseEntity<@NonNull ApiResponse<VerificationTokenResponse>> verifyAuthCode(@Valid @RequestBody VerificationCodeRequest request) {
        String token = verifyAuthCodeService.execute(request.email(), request.code());
        return ok("이메일 인증이 성공적으로 완료되었습니다.", new VerificationTokenResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<@NonNull ApiResponse<Void>> signUp(@Valid @RequestBody SignUpRequest request) {
        signUpService.execute(request);
        return ok("회원가입이 성공적으로 완료되었습니다.");
    }

    @PostMapping("/signin")
    public ResponseEntity<@NonNull ApiResponse<AuthTokenResponse>> signIn(@Valid @RequestBody SignInRequest request) {
        return ok("로그인이 성공적으로 완료되었습니다.", signInService.execute(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<@NonNull ApiResponse<AuthTokenResponse>> refresh(@Valid @RequestBody RefreshRequest request) {
        return ok("토큰이 성공적으로 재발급되었습니다.", refreshService.execute(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<@NonNull ApiResponse<Void>> logout(@Valid @RequestBody LogoutRequest request) {
        logoutService.execute(request);
        return ok("로그아웃이 성공적으로 완료되었습니다.");
    }
}
