package project.team.ondo.global.security.jwt.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.team.ondo.global.security.jwt.repository.RefreshTokenRepository;
import project.team.ondo.global.security.jwt.service.JwtRefreshTokenManagementService;

@Service
@RequiredArgsConstructor
public class JwtRefreshTokenManagementServiceImpl implements JwtRefreshTokenManagementService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void deleteRefreshToken(String refreshToken) {
        refreshTokenRepository.deleteById(refreshToken);
    }
}
