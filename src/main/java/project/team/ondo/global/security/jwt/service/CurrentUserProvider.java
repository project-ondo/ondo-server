package project.team.ondo.global.security.jwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.exception.UserNotFoundException;
import project.team.ondo.domain.user.exception.UserUnauthorizedException;
import project.team.ondo.domain.user.repository.UserRepository;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CurrentUserProvider {

    private final UserRepository userRepository;

    public UserEntity getCurrentUser() {

        var context = SecurityContextHolder.getContext();

        var authentication = context.getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserUnauthorizedException();
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof String publicIdStr)) {
            throw new UserUnauthorizedException();
        }

        UUID publicId;
        try {
            publicId = UUID.fromString(publicIdStr);
        } catch (NumberFormatException e) {
            throw new UserUnauthorizedException();
        }

        return userRepository.findByPublicId(publicId)
                .orElseThrow(UserNotFoundException::new);
    }
}
