package project.team.ondo.domain.auth.service;

import project.team.ondo.domain.auth.data.request.SignInRequest;
import project.team.ondo.domain.auth.data.response.AuthTokenResponse;

public interface SignInService {
    AuthTokenResponse execute(SignInRequest request);
}
