package project.team.ondo.domain.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.user.data.request.UpdateMyProfileImageRequest;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.exception.UserNotFoundException;
import project.team.ondo.domain.user.repository.UserRepository;
import project.team.ondo.domain.user.service.UpdateMyProfileImageService;

@Service
@RequiredArgsConstructor
public class UpdateMyProfileImageServiceImpl implements UpdateMyProfileImageService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public void execute(UserEntity me, UpdateMyProfileImageRequest request) {
        validateProfileKey(me, request.key());

        UserEntity managed = userRepository.findByPublicId(me.getPublicId())
                .orElseThrow(UserNotFoundException::new);
        managed.updateProfileImage(request.key());
    }

    private void validateProfileKey(UserEntity me, String key) {
        if (key == null || key.isBlank()) throw new IllegalArgumentException("KEY_REQUIRED");

        String requiredPrefix = "profile/" + me.getPublicId() + "/";
        if (!key.startsWith(requiredPrefix)) {
            throw new IllegalArgumentException("INVALID_PROFILE_IMAGE_KEY");
        }
    }
}
