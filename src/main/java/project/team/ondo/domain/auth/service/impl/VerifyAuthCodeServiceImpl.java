package project.team.ondo.domain.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
    public String execute(String email, String code) {
        AuthCodeEntity savedAuthCode = authCodeRepository.findById(email)
                .orElseThrow(AuthCodeExpiresException::new);

        if (savedAuthCode.getAttemptCount() >= 5) {
            throw new AttemptLimitExceededException();
        }

        if (!savedAuthCode.getCode().equals(code)) {
            savedAuthCode.increaseAttemptCount();
            authCodeRepository.save(savedAuthCode);
            if (savedAuthCode.getAttemptCount() >= 5) {
                throw new AttemptLimitExceededException();
            }
            throw new InvalidAuthCodeException();
        }

        authCodeRepository.deleteById(email);

        String token = UUID.randomUUID().toString();
        emailVerificationTokenRepository.save(
                EmailVerificationTokenEntity.builder()
                        .token(token)
                        .email(email)
                        .ttl(TOKEN_TTL_SECONDS)
                        .build()
        );

        return token;
    }
}
