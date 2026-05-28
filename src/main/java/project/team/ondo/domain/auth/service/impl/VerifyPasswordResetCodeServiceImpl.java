package project.team.ondo.domain.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.team.ondo.domain.auth.data.request.VerifyPasswordResetCodeRequest;
import project.team.ondo.domain.auth.data.response.PasswordResetTokenResponse;
import project.team.ondo.domain.auth.entity.PasswordResetCodeEntity;
import project.team.ondo.domain.auth.entity.PasswordResetTokenEntity;
import project.team.ondo.domain.auth.exception.AttemptLimitExceededException;
import project.team.ondo.domain.auth.exception.InvalidPasswordResetCodeException;
import project.team.ondo.domain.auth.exception.PasswordResetCodeExpiredException;
import project.team.ondo.domain.auth.repository.PasswordResetCodeRepository;
import project.team.ondo.domain.auth.repository.PasswordResetTokenRepository;
import project.team.ondo.domain.auth.service.VerifyPasswordResetCodeService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerifyPasswordResetCodeServiceImpl implements VerifyPasswordResetCodeService {

    private static final long RESET_TOKEN_TTL = 600L;
    private static final int MAX_ATTEMPTS = 5;

    private final PasswordResetCodeRepository passwordResetCodeRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public PasswordResetTokenResponse execute(VerifyPasswordResetCodeRequest request) {
        String email = request.email();
        String code = request.code();

        PasswordResetCodeEntity savedCode = passwordResetCodeRepository.findById(email)
                .orElseThrow(PasswordResetCodeExpiredException::new);

        if (savedCode.getAttemptCount() >= MAX_ATTEMPTS) {
            throw new AttemptLimitExceededException();
        }

        if (!savedCode.getCode().equals(code)) {
            savedCode.increaseAttemptCount();
            passwordResetCodeRepository.save(savedCode);
            if (savedCode.getAttemptCount() >= MAX_ATTEMPTS) {
                throw new AttemptLimitExceededException();
            }
            throw new InvalidPasswordResetCodeException();
        }

        passwordResetCodeRepository.deleteById(email);

        String resetToken = UUID.randomUUID().toString();
        passwordResetTokenRepository.save(
                PasswordResetTokenEntity.builder()
                        .token(resetToken)
                        .email(email)
                        .ttl(RESET_TOKEN_TTL)
                        .build()
        );

        return new PasswordResetTokenResponse(resetToken);
    }
}
