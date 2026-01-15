package project.team.ondo.domain.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.user.data.response.MyProfileResponse;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.service.GetMyProfileService;
import project.team.ondo.global.security.jwt.service.CurrentUserProvider;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class GetMyProfileServiceImpl implements GetMyProfileService {

    private final CurrentUserProvider currentUserProvider;

    @Transactional(readOnly = true)
    @Override
    public MyProfileResponse execute() {
        UserEntity user = currentUserProvider.getCurrentUser();
        return new MyProfileResponse(
                user.getPublicId(),
                user.getLoginId(),
                user.getEmail(),
                user.getDisplayName(),
                user.getGender(),
                user.getMajor(),
                new ArrayList<>(user.getInterests()),
                user.getProfileImageKey(),
                user.getBio(),
                user.getRole(),
                user.getStatus()
        );
    }
}
