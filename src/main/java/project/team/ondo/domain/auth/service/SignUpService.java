package project.team.ondo.domain.auth.service;

import project.team.ondo.domain.auth.data.request.SignUpRequest;

public interface SignUpService {
    void execute(SignUpRequest request);
}
