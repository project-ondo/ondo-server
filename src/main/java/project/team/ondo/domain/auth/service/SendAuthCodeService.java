package project.team.ondo.domain.auth.service;

import project.team.ondo.domain.auth.data.request.SendEmailRequest;

public interface SendAuthCodeService {
    void execute(SendEmailRequest request);
}
