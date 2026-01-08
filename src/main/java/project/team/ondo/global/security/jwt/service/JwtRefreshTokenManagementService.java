package project.team.ondo.global.security.jwt.service;

public interface JwtRefreshTokenManagementService {
    void execute(String refreshToken);
}
