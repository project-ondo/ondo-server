package project.team.ondo.domain.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.auth.exception.DisplayNameDuplicatedException;
import project.team.ondo.domain.user.data.request.UpdateMyProfileRequest;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.repository.UserRepository;
import project.team.ondo.domain.user.service.UpdateMyProfileService;
import project.team.ondo.global.security.jwt.service.CurrentUserProvider;

@Service
@RequiredArgsConstructor
public class UpdateMyProfileServiceImpl implements UpdateMyProfileService {

    private final CurrentUserProvider currentUserProvider;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void execute(UpdateMyProfileRequest request) {
        UserEntity user = currentUserProvider.getCurrentUser();

        if (!user.getDisplayName().equals(request.displayName()) && userRepository.existsByDisplayName(request.displayName())) {
            throw new DisplayNameDuplicatedException();
        }

        user.updateProfile(
                request.displayName(),
                request.gender(),
                request.major(),
                request.interests(),
                request.profileImageUrl(),
                request.bio()
        );
    }
}
