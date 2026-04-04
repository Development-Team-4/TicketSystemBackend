package development.team.ticketsystem.authservice.service;

import development.team.ticketsystem.authservice.config.JwtProperties;
import development.team.ticketsystem.authservice.dto.auth.AuthResponse;
import development.team.ticketsystem.authservice.entity.RefreshToken;
import development.team.ticketsystem.authservice.entity.User;
import development.team.ticketsystem.authservice.exception.InvalidTokenException;
import development.team.ticketsystem.authservice.exception.RefreshTokenExpiredException;
import development.team.ticketsystem.authservice.exception.RefreshTokenRevokedException;
import development.team.ticketsystem.authservice.mapper.UserMapper;
import development.team.ticketsystem.authservice.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenService jwtTokenService;
    private final JwtProperties jwtProperties;
    private final UserMapper userMapper;

    @Transactional
    public AuthResponse issueTokens(User user, String ip, String userAgent) {
        String accessJti = jwtTokenService.generateAccessJti();
        String accessToken = jwtTokenService.generateAccessToken(user, accessJti);

        String refreshRaw = jwtTokenService.generateRefreshTokenRaw();
        String refreshHash = jwtTokenService.hashToken(refreshRaw);

        RefreshToken refreshToken = RefreshToken.builder()
                .tokenHash(refreshHash)
                .jti(jwtTokenService.generateRefreshJti())
                .userId(user.getId())
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(jwtProperties.getRefreshTokenTtlSeconds()))
                .createdByIp(ip)
                .userAgent(userAgent)
                .build();

        refreshTokenRepository.save(refreshToken);

        return new AuthResponse(
                accessToken,
                "Bearer",
                refreshRaw,
                jwtProperties.getAccessTokenTtlSeconds(),
                accessJti
        );
    }

    @Transactional
    public AuthResponse refresh(User user, String rawRefreshToken) {
        RefreshToken stored = getValidRefreshToken(rawRefreshToken);

        stored.setRevokedAt(Instant.now());
        refreshTokenRepository.save(stored);

        return issueTokens(user, stored.getCreatedByIp(), stored.getUserAgent());
    }

    @Transactional
    public void revoke(String rawRefreshToken) {
        String refreshHash = jwtTokenService.hashToken(rawRefreshToken);

        RefreshToken stored = refreshTokenRepository.findByTokenHash(refreshHash)
                .orElseThrow(InvalidTokenException::new);

        if (!stored.isRevoked()) {
            stored.setRevokedAt(Instant.now());
            refreshTokenRepository.save(stored);
        }
    }

    public RefreshToken getValidRefreshToken(String rawRefreshToken) {
        String refreshHash = jwtTokenService.hashToken(rawRefreshToken);

        RefreshToken stored = refreshTokenRepository.findByTokenHash(refreshHash)
                .orElseThrow(InvalidTokenException::new);

        if (stored.isRevoked()) {
            throw new RefreshTokenRevokedException();
        }

        if (stored.isExpired()) {
            throw new RefreshTokenExpiredException();
        }

        return stored;
    }

    public Duration accessBlacklistTtl() {
        return Duration.ofSeconds(jwtProperties.getAccessTokenTtlSeconds());
    }
}