package project.team.ondo.global.security.jwt.service;

import jakarta.servlet.http.HttpServletRequest;
import project.team.ondo.domain.user.constant.UserRole;

public interface JwtParserService {
    Boolean validateAccessToken(String accessToken);

    Boolean validateRefreshToken(String refreshToken);

    String getUserIdFromAccessToken(String accessToken);

    String getUserIdFromRefreshToken(String refreshToken);

    UserRole getRoleFromAccessToken(String accessToken);

    String resolveToken(HttpServletRequest request);
}
