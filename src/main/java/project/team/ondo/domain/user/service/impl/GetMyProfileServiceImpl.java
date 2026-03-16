package project.team.ondo.domain.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.user.data.response.MyProfileResponse;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.service.GetMyProfileService;


@Service
@RequiredArgsConstructor
public class GetMyProfileServiceImpl implements GetMyProfileService {

    @Transactional(readOnly = true)
    @Override
    public MyProfileResponse execute(UserEntity user) {
        return MyProfileResponse.from(user);
    }
}
