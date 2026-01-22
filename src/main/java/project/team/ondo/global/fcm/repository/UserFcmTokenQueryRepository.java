package project.team.ondo.global.fcm.repository;

import java.util.List;
import java.util.UUID;

public interface UserFcmTokenQueryRepository {
    List<String> findActiveTokens(UUID userPublicId);

    long deactivateToken(UUID userPublicId, String token);

    long deactivateAll(UUID userPublicId);
}
