package project.team.ondo.domain.auth.service;

import project.team.ondo.domain.auth.data.request.VerificationCodeRequest;

public interface VerifyAuthCodeService {
    String execute(VerificationCodeRequest request);
}
