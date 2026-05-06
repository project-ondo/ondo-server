package project.team.ondo.domain.auth.service;

import project.team.ondo.domain.auth.data.request.ReactivateRequest;
import project.team.ondo.domain.auth.data.response.AuthTokenResponse;

public interface ReactivateService {
    AuthTokenResponse execute(ReactivateRequest request);
}