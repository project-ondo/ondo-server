package project.team.ondo.domain.rating.service;

import java.util.UUID;

public interface RateUserService {
    void execute(UUID chatRoomPublicId, int stars, String comment);
}
