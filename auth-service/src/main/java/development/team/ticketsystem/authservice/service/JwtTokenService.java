package development.team.ticketsystem.authservice.service;

import development.team.ticketsystem.authservice.config.JwtProperties;
import development.team.ticketsystem.authservice.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtEncoder jwtEncoder;
    private final JwtProperties properties;

    public String generateAccessToken(User user, String accessJti) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(properties.getIssuer())
                .subject(user.getId().toString())
                .audience(List.of(properties.getAudience()))
                .issuedAt(now)
                .expiresAt(now.plusSeconds(properties.getAccessTokenTtlSeconds()))
                .id(accessJti)
                .claim("userId", user.getId())
                .claim("roles", List.of(user.getRole()))
                .claim("token_type", "access")
                .build();

        JwsHeader header = JwsHeader.with(SignatureAlgorithm.RS256)
                .keyId(properties.getKeyId())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims))
                .getTokenValue();
    }

    public String generateRefreshTokenRaw() {
        return UUID.randomUUID() + "." + UUID.randomUUID();
    }

    public String generateRefreshJti() {
        return UUID.randomUUID().toString();
    }

    public String generateAccessJti() {
        return UUID.randomUUID().toString();
    }

    public String hashToken(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot hash token", e);
        }
    }
}