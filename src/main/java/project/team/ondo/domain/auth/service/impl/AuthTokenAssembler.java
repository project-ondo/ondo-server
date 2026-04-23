package project.team.ondo.domain.auth.service.impl;

import project.team.ondo.domain.auth.data.response.AuthTokenResponse;
import project.team.ondo.global.data.AuthToken;

final class AuthTokenAssembler {
    private AuthTokenAssembler() {}

    static AuthTokenResponse assemble(AuthToken access, AuthToken refresh) {
        return new AuthTokenResponse(
                access.token(),
                access.expiration(),
                refresh.token(),
                refresh.expiration()
        );
    }
}
