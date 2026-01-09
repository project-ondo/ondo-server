package project.team.ondo.domain.auth.service;

import project.team.ondo.domain.auth.data.request.RefreshRequest;
import project.team.ondo.domain.auth.data.response.AuthTokenResponse;

public interface RefreshService {
    AuthTokenResponse execute(RefreshRequest request);
}
