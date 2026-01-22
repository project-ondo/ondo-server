package project.team.ondo.global.security.jwt.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.team.ondo.domain.user.constant.UserRole;
import project.team.ondo.global.data.JwtEnvironment;
import project.team.ondo.global.security.jwt.repository.RefreshTokenRepository;
import project.team.ondo.global.security.jwt.service.JwtParserService;

import javax.crypto.SecretKey;

@Service
@RequiredArgsConstructor
public class JwtParserServiceImpl implements JwtParserService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtEnvironment jwtEnvironment;

    private SecretKey accessTokenKey;
    private SecretKey refreshTokenKey;

    @PostConstruct
    public void init() {
        accessTokenKey = Keys.hmacShaKeyFor(jwtEnvironment.accessToken().secret().getBytes());
        refreshTokenKey = Keys.hmacShaKeyFor(jwtEnvironment.refreshToken().secret().getBytes());
    }

    @Override
    public Boolean validateAccessToken(String accessToken) {
        try {
            parseAccessTokenClaims(accessToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean validateRefreshToken(String refreshToken) {
        try {
            parseRefreshTokenClaims(refreshToken);
            return refreshTokenRepository.existsById(refreshToken);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getUserIdFromAccessToken(String accessToken) {
        return parseAccessTokenClaims(accessToken).getSubject();
    }

    @Override
    public String getUserIdFromRefreshToken(String refreshToken) {
        return parseRefreshTokenClaims(refreshToken).getSubject();
    }

    @Override
    public UserRole getRoleFromAccessToken(String accessToken) {
        return UserRole.valueOf(parseAccessTokenClaims(accessToken).get("role", String.class));
    }

    @Override
    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return (token != null && token.startsWith("Bearer ")) ? token.substring(7) : null;
    }

    private Claims parseAccessTokenClaims(String accessToken) {
        return Jwts.parser()
                .verifyWith(accessTokenKey)
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();
    }

    private Claims parseRefreshTokenClaims(String refreshToken) {
        return Jwts.parser()
                .verifyWith(refreshTokenKey)
                .build()
                .parseSignedClaims(refreshToken)
                .getPayload();
    }
}
