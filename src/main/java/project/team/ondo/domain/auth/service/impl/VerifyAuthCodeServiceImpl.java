package project.team.ondo.domain.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.team.ondo.domain.auth.data.request.VerificationCodeRequest;
import project.team.ondo.domain.auth.entity.AuthCodeEntity;
import project.team.ondo.domain.auth.entity.EmailVerificationTokenEntity;
import project.team.ondo.domain.auth.exception.AttemptLimitExceededException;
import project.team.ondo.domain.auth.exception.AuthCodeExpiresException;
import project.team.ondo.domain.auth.exception.InvalidAuthCodeException;
import project.team.ondo.domain.auth.repository.AuthCodeRepository;
import project.team.ondo.domain.auth.repository.EmailVerificationTokenRepository;
import project.team.ondo.domain.auth.service.VerifyAuthCodeService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerifyAuthCodeServiceImpl implements VerifyAuthCodeService {

    private static final long TOKEN_TTL_SECONDS = 1800L;

    private final AuthCodeRepository authCodeRepository;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Override
    public String execute(VerificationCodeRequest request) {
        AuthCodeEntity savedAuthCode = authCodeRepository.findById(request.email())
                .orElseThrow(AuthCodeExpiresException::new);

        int attempt = savedAuthCode.getAttemptCount() == null ? 0 : savedAuthCode.getAttemptCount();

        if (attempt >= 5) {
            throw new AttemptLimitExceededException();
        }

        if (!savedAuthCode.getCode().equals(request.code())) {
            savedAuthCode.increaseAttemptCount();
            authCodeRepository.save(savedAuthCode);

            int updatedAttempt = savedAuthCode.getAttemptCount() == null ? 0 : savedAuthCode.getAttemptCount();
            if (updatedAttempt >= 5) {
                throw new AttemptLimitExceededException();
            }
            throw new InvalidAuthCodeException();
        }

        authCodeRepository.deleteById(request.email());

        String token = UUID.randomUUID().toString();
        emailVerificationTokenRepository.save(
                EmailVerificationTokenEntity.builder()
                        .token(token)
                        .email(request.email())
                        .ttl(TOKEN_TTL_SECONDS)
                        .build()
        );

        return token;
    }
}
