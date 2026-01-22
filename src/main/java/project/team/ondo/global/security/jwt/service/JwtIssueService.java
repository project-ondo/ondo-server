package project.team.ondo.global.security.jwt.service;

import project.team.ondo.domain.user.constant.UserRole;
import project.team.ondo.global.data.AuthToken;

import java.util.UUID;

public interface JwtIssueService {
    AuthToken issueAccessToken(UUID publicId, UserRole role);
    AuthToken issueRefreshToken(UUID publicId);
}
