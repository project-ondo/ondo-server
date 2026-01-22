package project.team.ondo.domain.auth.service;

import project.team.ondo.domain.auth.data.request.SIgnInRequest;
import project.team.ondo.domain.auth.data.response.AuthTokenResponse;

public interface SignInService {
    AuthTokenResponse execute(SIgnInRequest request);
}
