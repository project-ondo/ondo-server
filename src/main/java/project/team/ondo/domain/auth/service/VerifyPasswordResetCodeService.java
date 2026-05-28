package project.team.ondo.domain.auth.service;

import project.team.ondo.domain.auth.data.request.VerifyPasswordResetCodeRequest;
import project.team.ondo.domain.auth.data.response.PasswordResetTokenResponse;

public interface VerifyPasswordResetCodeService {
    PasswordResetTokenResponse execute(VerifyPasswordResetCodeRequest request);
}