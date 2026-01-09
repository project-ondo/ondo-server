package project.team.ondo.domain.auth.service;

import project.team.ondo.domain.auth.data.request.LogoutRequest;

public interface LogoutService {
    void execute(LogoutRequest request);
}
