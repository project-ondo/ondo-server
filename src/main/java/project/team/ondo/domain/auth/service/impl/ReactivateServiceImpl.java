package project.team.ondo.domain.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.auth.data.request.ReactivateRequest;
import project.team.ondo.domain.auth.data.response.AuthTokenResponse;
import project.team.ondo.domain.auth.exception.AccountNotWithdrawnException;
import project.team.ondo.domain.auth.exception.LoginFailedException;
import project.team.ondo.domain.auth.exception.ReactivationPeriodExpiredException;
import project.team.ondo.domain.auth.service.ReactivateService;
import project.team.ondo.domain.user.constant.UserStatus;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.repository.UserRepository;
import project.team.ondo.global.data.AuthToken;
import project.team.ondo.global.security.jwt.service.JwtIssueService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReactivateServiceImpl implements ReactivateService {

    private static final int GRACE_PERIOD_DAYS = 30;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtIssueService jwtIssueService;

    @Transactional
    @Override
    public AuthTokenResponse execute(ReactivateRequest request) {
        UserEntity user = userRepository.findByLoginId(request.loginId())
                .orElseThrow(LoginFailedException::new);

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new LoginFailedException();
        }

        if (user.getStatus() != UserStatus.DELETED) {
            throw new AccountNotWithdrawnException();
        }

        if (user.getDeletedAt().isBefore(LocalDateTime.now().minusDays(GRACE_PERIOD_DAYS))) {
            throw new ReactivationPeriodExpiredException();
        }

        user.reactivate();

        AuthToken accessToken = jwtIssueService.issueAccessToken(user.getPublicId(), user.getRole());
        AuthToken refreshToken = jwtIssueService.issueRefreshToken(user.getPublicId());
        return AuthTokenAssembler.assemble(accessToken, refreshToken);
    }
}