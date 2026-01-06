package project.team.ondo.domain.auth.controller;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.team.ondo.domain.auth.data.request.SendEmailRequest;
import project.team.ondo.domain.auth.data.request.VerificationCodeRequest;
import project.team.ondo.domain.auth.data.response.VerificationTokenResponse;
import project.team.ondo.domain.auth.service.SendAuthCodeService;
import project.team.ondo.domain.auth.service.VerifyAuthCodeService;
import project.team.ondo.global.response.ApiResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SendAuthCodeService sendAuthCodeService;
    private final VerifyAuthCodeService verifyAuthCodeService;

    @PostMapping("/email/send")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<@NonNull ApiResponse<Void>> sendAuthCode(@Valid @RequestBody SendEmailRequest request) {
        sendAuthCodeService.execute(request.email());
        return ResponseEntity.ok(
                ApiResponse.success(
                        "인증코드가 성공적으로 발송되었습니다."
                )
        );
    }

    @PatchMapping("/email/verify")
    public ResponseEntity<@NonNull ApiResponse<VerificationTokenResponse>> verifyAuthCode(@Valid @RequestBody VerificationCodeRequest request) {
        String token = verifyAuthCodeService.execute(request.email(), request.code());
        return ResponseEntity.ok(
                ApiResponse.success(
                        "이메일 인증이 성공적으로 완료되었습니다.",
                        new VerificationTokenResponse(token)
                )
        );
    }
}
