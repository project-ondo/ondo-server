package project.team.ondo.domain.user.service;

import project.team.ondo.domain.user.data.request.UpdateMyProfileRequest;
import project.team.ondo.domain.user.entity.UserEntity;

public interface UpdateMyProfileService {
    void execute(UserEntity me, UpdateMyProfileRequest request);
}
