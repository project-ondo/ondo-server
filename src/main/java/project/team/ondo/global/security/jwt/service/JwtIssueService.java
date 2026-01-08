package project.team.ondo.global.security.jwt.service;

import project.team.ondo.domain.user.constant.UserRole;
import project.team.ondo.global.data.AuthToken;

public interface JwtIssueService {
    AuthToken issueAccessToken(long userId, UserRole role);
    AuthToken issueRefreshToken(long userId);
}
