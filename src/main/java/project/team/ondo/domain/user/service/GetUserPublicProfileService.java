package project.team.ondo.domain.user.service;

import project.team.ondo.domain.user.data.response.UserPublicProfileResponse;

import java.util.UUID;

public interface GetUserPublicProfileService {
    UserPublicProfileResponse execute(UUID publicId);
}
