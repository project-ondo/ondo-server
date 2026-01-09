package project.team.ondo.domain.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.auth.data.request.SignUpRequest;
import project.team.ondo.domain.auth.entity.EmailVerificationTokenEntity;
import project.team.ondo.domain.auth.exception.*;
import project.team.ondo.domain.auth.repository.EmailVerificationTokenRepository;
import project.team.ondo.domain.auth.service.SignUpService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.repository.UserRepository;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {

    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void execute(SignUpRequest request) {
        EmailVerificationTokenEntity verificationToken = emailVerificationTokenRepository.findById(request.verificationToken())
                .orElseThrow(InvalidEmailVerificationTokenException::new);

        emailVerificationTokenRepository.deleteById(request.verificationToken());

        String email = verificationToken.getEmail();

        if (userRepository.existsByEmail(email)) throw new EmailAlreadyExistsException();
        if (userRepository.existsByLoginId(request.loginId())) throw new LoginIdDuplicatedException();
        if (userRepository.existsByDisplayName(request.displayName())) throw new DisplayNameDuplicatedException();

        String passwordHash = passwordEncoder.encode(request.password());

        UserEntity user = UserEntity.create(
                request.loginId(),
                email,
                passwordHash,
                request.displayName(),
                request.gender(),
                request.major(),
                request.interests() != null ? request.interests() : new ArrayList<>(),
                request.profileImageUrl()
        );

        userRepository.save(user);
    }
}
