package project.team.ondo.domain.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.auth.data.request.SignInRequest;
import project.team.ondo.domain.auth.data.response.AuthTokenResponse;
import project.team.ondo.domain.auth.exception.LoginFailedException;
import project.team.ondo.domain.auth.exception.UserSuspendedException;
import project.team.ondo.domain.auth.service.SignInService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.entity.UserSuspensionEntity;
import project.team.ondo.domain.user.repository.UserRepository;
import project.team.ondo.domain.user.repository.UserSuspensionRepository;
import project.team.ondo.global.data.AuthToken;
import project.team.ondo.global.security.jwt.service.JwtIssueService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SignInServiceImpl implements SignInService {

    private final UserRepository userRepository;
    private final UserSuspensionRepository userSuspensionRepository;
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

        userSuspensionRepository.findByUserPublicId(user.getPublicId())
                .map(UserSuspensionEntity::getSuspendedUntil)
                .filter(until -> until.isAfter(LocalDateTime.now()))
                .ifPresent(until -> { throw new UserSuspendedException(until); });

        AuthToken accessToken = jwtIssueService.issueAccessToken(user.getPublicId(), user.getRole());
        AuthToken refreshToken = jwtIssueService.issueRefreshToken(user.getPublicId());
        return AuthTokenAssembler.assemble(accessToken, refreshToken);
    }
}
