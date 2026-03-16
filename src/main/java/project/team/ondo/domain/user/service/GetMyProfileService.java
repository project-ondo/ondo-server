package project.team.ondo.domain.user.service;

import project.team.ondo.domain.user.data.response.MyProfileResponse;
import project.team.ondo.domain.user.entity.UserEntity;

public interface GetMyProfileService {
    MyProfileResponse execute(UserEntity me);
}
