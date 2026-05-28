package project.team.ondo.domain.auth.service;

import project.team.ondo.domain.auth.data.request.SendPasswordResetCodeRequest;

public interface SendPasswordResetCodeService {
    void execute(SendPasswordResetCodeRequest request);
}