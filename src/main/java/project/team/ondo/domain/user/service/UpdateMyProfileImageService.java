package project.team.ondo.domain.user.service;

import project.team.ondo.domain.user.data.request.UpdateMyProfileImageRequest;
import project.team.ondo.domain.user.entity.UserEntity;

public interface UpdateMyProfileImageService {
    void execute(UserEntity me, UpdateMyProfileImageRequest request);
}
