package project.team.ondo.domain.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.user.data.response.MyProfileResponse;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.exception.UserNotFoundException;
import project.team.ondo.domain.user.repository.UserRepository;
import project.team.ondo.domain.user.service.GetMyProfileService;


@Service
@RequiredArgsConstructor
public class GetMyProfileServiceImpl implements GetMyProfileService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public MyProfileResponse execute(UserEntity user) {
        UserEntity managed = userRepository.findByPublicId(user.getPublicId())
                .orElseThrow(UserNotFoundException::new);
        return MyProfileResponse.from(managed);
    }
}
