package project.team.ondo.domain.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.team.ondo.domain.auth.entity.AuthCodeEntity;
import project.team.ondo.domain.auth.exception.EmailAlreadyExistsException;
import project.team.ondo.domain.auth.repository.AuthCodeRepository;
import project.team.ondo.domain.auth.service.SendAuthCodeService;
import project.team.ondo.domain.auth.service.SendEmailService;
import project.team.ondo.domain.user.repository.UserRepository;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class SendAuthCodeServiceImpl implements SendAuthCodeService {

    private static final long AUTH_CODE_TTL = 300L;

    private final AuthCodeRepository authCodeRepository;
    private final UserRepository userRepository;
    private final SendEmailService sendEmailService;

    private final SecureRandom random = new SecureRandom();

    @Override
    public void execute(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException();
        }

        String authCode = String.format("%06d", random.nextInt(1_000_000));

        authCodeRepository.save(
                AuthCodeEntity.builder()
                        .email(email)
                        .code(authCode)
                        .ttl(AUTH_CODE_TTL)
                        .build()
        );

        sendEmailService.sendAuthCode(email, authCode);
    }
}
