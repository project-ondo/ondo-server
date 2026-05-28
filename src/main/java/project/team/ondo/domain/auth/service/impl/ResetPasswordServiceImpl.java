package project.team.ondo.domain.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.auth.data.request.ResetPasswordRequest;
import project.team.ondo.domain.auth.entity.PasswordResetTokenEntity;
import project.team.ondo.domain.auth.exception.InvalidPasswordResetTokenException;
import project.team.ondo.domain.auth.repository.PasswordResetTokenRepository;
import project.team.ondo.domain.auth.service.ResetPasswordService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.repository.UserRepository;
import project.team.ondo.global.security.jwt.repository.RefreshTokenRepository;
import project.team.ondo.global.security.jwt.repository.UserRefreshTokenIndexRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ResetPasswordServiceImpl implements ResetPasswordService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRefreshTokenIndexRepository userRefreshTokenIndexRepository;

    @Transactional
    @Override
    public void execute(ResetPasswordRequest request) {
        PasswordResetTokenEntity resetToken = passwordResetTokenRepository.findById(request.resetToken())
                .orElseThrow(InvalidPasswordResetTokenException::new);

        UserEntity user = userRepository.findByEmail(resetToken.getEmail())
                .orElseThrow(InvalidPasswordResetTokenException::new);

        user.updatePassword(passwordEncoder.encode(request.newPassword()));

        String userId = user.getPublicId().toString();
        Set<String> existingTokens = userRefreshTokenIndexRepository.findAll(userId);
        existingTokens.forEach(refreshTokenRepository::deleteById);
        userRefreshTokenIndexRepository.deleteAll(userId);

        passwordResetTokenRepository.deleteById(request.resetToken());
    }
}