package project.team.ondo.domain.rating.service;

import project.team.ondo.domain.user.entity.UserEntity;

import java.util.UUID;

public interface RateUserService {
    void execute(UserEntity me, UUID chatRoomPublicId, int stars, String comment);
}
