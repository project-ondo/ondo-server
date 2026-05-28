package project.team.ondo.domain.auth.service;

import project.team.ondo.domain.auth.data.request.ResetPasswordRequest;

public interface ResetPasswordService {
    void execute(ResetPasswordRequest request);
}