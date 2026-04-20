package project.team.ondo.domain.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.auth.data.request.RefreshRequest;
import project.team.ondo.domain.auth.data.response.AuthTokenResponse;
import project.team.ondo.domain.auth.exception.InvalidTokenException;
import project.team.ondo.domain.auth.service.RefreshService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.repository.UserRepository;
import project.team.ondo.global.data.AuthToken;
import project.team.ondo.global.security.jwt.service.JwtIssueService;
import project.team.ondo.global.security.jwt.service.JwtParserService;
import project.team.ondo.global.security.jwt.service.JwtRefreshTokenManagementService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshServiceImpl implements RefreshService {

    private final JwtParserService jwtParserService;
    private final UserRepository userRepository;
    private final JwtIssueService jwtIssueService;
    private final JwtRefreshTokenManagementService jwtRefreshTokenManagementService;

    @Transactional
    @Override
    public AuthTokenResponse execute(RefreshRequest request) {
        String refreshToken = request.refreshToken();

        if (!jwtParserService.validateRefreshToken(refreshToken)) throw new InvalidTokenException();

        UUID publicId = UUID.fromString(jwtParserService.getUserIdFromRefreshToken(refreshToken));

        UserEntity user = userRepository.getByPublicId(publicId);

        jwtRefreshTokenManagementService.execute(refreshToken);

        AuthToken newAccessToken = jwtIssueService.issueAccessToken(publicId, user.getRole());
        AuthToken newRefreshToken = jwtIssueService.issueRefreshToken(publicId);

        return AuthTokenAssembler.assemble(newAccessToken, newRefreshToken);
    }
}
