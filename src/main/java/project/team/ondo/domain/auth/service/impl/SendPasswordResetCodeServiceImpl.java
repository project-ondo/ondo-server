package project.team.ondo.domain.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.team.ondo.domain.auth.data.request.SendPasswordResetCodeRequest;
import project.team.ondo.domain.auth.entity.PasswordResetCodeEntity;
import project.team.ondo.domain.auth.exception.EmailNotFoundException;
import project.team.ondo.domain.auth.exception.PasswordResetCodeAlreadySentException;
import project.team.ondo.domain.auth.repository.PasswordResetCodeRepository;
import project.team.ondo.domain.auth.service.SendEmailService;
import project.team.ondo.domain.auth.service.SendPasswordResetCodeService;
import project.team.ondo.domain.user.repository.UserRepository;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class SendPasswordResetCodeServiceImpl implements SendPasswordResetCodeService {

    private static final long PASSWORD_RESET_CODE_TTL = 300L;

    private final PasswordResetCodeRepository passwordResetCodeRepository;
    private final UserRepository userRepository;
    private final SendEmailService sendEmailService;

    private final SecureRandom random = new SecureRandom();

    @Override
    public void execute(SendPasswordResetCodeRequest request) {
        String email = request.email();

        if (!userRepository.existsByEmail(email)) {
            throw new EmailNotFoundException();
        }

        if (passwordResetCodeRepository.existsById(email)) {
            throw new PasswordResetCodeAlreadySentException();
        }

        String code = String.format("%06d", random.nextInt(1_000_000));

        passwordResetCodeRepository.save(
                PasswordResetCodeEntity.builder()
                        .email(email)
                        .code(code)
                        .ttl(PASSWORD_RESET_CODE_TTL)
                        .build()
        );

        sendEmailService.sendPasswordResetCode(email, code);
    }
}