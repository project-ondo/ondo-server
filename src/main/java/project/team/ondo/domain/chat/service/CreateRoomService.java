package project.team.ondo.domain.chat.service;

import project.team.ondo.domain.user.entity.UserEntity;

import java.util.UUID;

public interface CreateRoomService {
    UUID execute(UserEntity me, UUID targetUserPublicId);
}
