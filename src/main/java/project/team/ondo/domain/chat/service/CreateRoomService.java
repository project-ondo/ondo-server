package project.team.ondo.domain.chat.service;

import java.util.UUID;

public interface CreateRoomService {
    UUID execute(UUID targetUserPublicId);
}
