package project.team.ondo.global.security.jwt.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.team.ondo.domain.user.constant.UserRole;
import project.team.ondo.global.data.AuthToken;
import project.team.ondo.global.data.JwtEnvironment;
import project.team.ondo.global.security.jwt.entity.RefreshTokenEntity;
import project.team.ondo.global.security.jwt.repository.RefreshTokenRepository;
import project.team.ondo.global.security.jwt.service.JwtIssueService;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtIssueServiceImpl implements JwtIssueService {

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
    public AuthToken issueAccessToken(long userId, UserRole role) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(jwtEnvironment.accessToken().expiration());
        return new AuthToken(
                Jwts.builder()
                        .subject(Long.toString(userId))
                        .claim("role", role.name())
                        .issuedAt(Date.from(now))
                        .expiration(Date.from(expiration))
                        .signWith(accessTokenKey)
                        .compact(),
                LocalDateTime.ofInstant(expiration, ZoneId.of("Asia/Seoul"))
        );
    }

    @Override
    public AuthToken issueRefreshToken(long userId) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(jwtEnvironment.refreshToken().expiration());
        AuthToken token = new AuthToken(
                Jwts.builder()
                        .subject(Long.toString(userId))
                        .issuedAt(Date.from(now))
                        .expiration(Date.from(expiration))
                        .signWith(refreshTokenKey)
                        .compact(),
                LocalDateTime.ofInstant(expiration, ZoneId.of("Asia/Seoul"))
        );

        long ttlSeconds = ChronoUnit.SECONDS.between(now, expiration);
        refreshTokenRepository.save(RefreshTokenEntity.builder()
                .token(token.token())
                .expiration(ttlSeconds)
                .build()
        );
        return token;
    }
}
