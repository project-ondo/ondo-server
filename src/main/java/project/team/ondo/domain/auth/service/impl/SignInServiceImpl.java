package project.team.ondo.domain.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.auth.data.request.SignInRequest;
import project.team.ondo.domain.auth.data.response.AuthTokenResponse;
import project.team.ondo.domain.auth.exception.LoginFailedException;
import project.team.ondo.domain.auth.service.SignInService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.repository.UserRepository;
import project.team.ondo.global.data.AuthToken;
import project.team.ondo.global.security.jwt.service.JwtIssueService;

@Service
@RequiredArgsConstructor
public class SignInServiceImpl implements SignInService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtIssueService jwtIssueService;

    @Transactional
    @Override
    public AuthTokenResponse execute(SignInRequest request) {
        UserEntity user = userRepository.findByLoginId(request.loginId())
                .orElseThrow(LoginFailedException::new);

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new LoginFailedException();
        }

        AuthToken accessToken = jwtIssueService.issueAccessToken(user.getPublicId(), user.getRole());
        AuthToken refreshToken = jwtIssueService.issueRefreshToken(user.getPublicId());

        return new AuthTokenResponse(
                accessToken.token(),
                accessToken.expiration(),
                refreshToken.token(),
                refreshToken.expiration()
        );
    }
}
