package project.team.ondo.domain.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.auth.data.request.LogoutRequest;
import project.team.ondo.domain.auth.exception.InvalidTokenException;
import project.team.ondo.domain.auth.service.LogoutService;
import project.team.ondo.global.security.jwt.service.JwtParserService;
import project.team.ondo.global.security.jwt.service.JwtRefreshTokenManagementService;

@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutService {

    private final JwtParserService jwtParserService;
    private final JwtRefreshTokenManagementService jwtRefreshTokenManagementService;

    @Transactional
    @Override
    public void execute(LogoutRequest request) {
        String refreshToken = request.refreshToken();

        if (!jwtParserService.validateRefreshToken(refreshToken)) {
            throw new InvalidTokenException();
        }

        jwtRefreshTokenManagementService.execute(refreshToken);
    }
}
