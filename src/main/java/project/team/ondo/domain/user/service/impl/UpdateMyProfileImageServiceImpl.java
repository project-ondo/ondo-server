package project.team.ondo.domain.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.user.data.request.UpdateMyProfileImageRequest;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.service.UpdateMyProfileImageService;
import project.team.ondo.global.security.jwt.service.CurrentUserProvider;

@Service
@RequiredArgsConstructor
public class UpdateMyProfileImageServiceImpl implements UpdateMyProfileImageService {

    private final CurrentUserProvider currentUserProvider;

    @Transactional
    @Override
    public void execute(UpdateMyProfileImageRequest request) {
        UserEntity me = currentUserProvider.getCurrentUser();

        validateProfileKey(me, request.key());

        me.updateProfileImage(request.key());
    }

    private void validateProfileKey(UserEntity me, String key) {
        if (key == null || key.isBlank()) throw new IllegalArgumentException("KEY_REQUIRED");

        String requiredPrefix = "profile/" + me.getPublicId() + "/";
        if (!key.startsWith(requiredPrefix)) {
            throw new IllegalArgumentException("INVALID_PROFILE_IMAGE_KEY");
        }
    }
}
