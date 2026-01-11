package project.team.ondo.domain.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.user.data.response.UserPublicProfileResponse;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.exception.UserNotFoundException;
import project.team.ondo.domain.user.repository.UserRepository;
import project.team.ondo.domain.user.service.GetUserPublicProfileService;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetUserPublicProfileServiceImpl implements GetUserPublicProfileService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public UserPublicProfileResponse execute(UUID publicId) {
        UserEntity user = userRepository.findByPublicId(publicId)
                .orElseThrow(UserNotFoundException::new);

        return new UserPublicProfileResponse(
                user.getPublicId(),
                user.getDisplayName(),
                user.getGender(),
                user.getMajor(),
                new ArrayList<>(user.getInterests()),
                user.getProfileImageUrl(),
                user.getBio(),
                user.getRole()
        );
    }
}
