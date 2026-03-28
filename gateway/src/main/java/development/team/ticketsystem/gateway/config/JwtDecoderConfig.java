package development.team.ticketsystem.gateway.config;

import development.team.ticketsystem.gateway.properties.GatewayJwtProperties;
import development.team.ticketsystem.gateway.security.RedisTokenBlacklistReaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.jwt.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(GatewayJwtProperties.class)
public class JwtDecoderConfig {

    private final GatewayJwtProperties properties;
    private final RedisTokenBlacklistReaderService blacklistReaderService;

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        NimbusReactiveJwtDecoder delegate =
                NimbusReactiveJwtDecoder.withJwkSetUri(properties.getJwkSetUri()).build();

        OAuth2TokenValidator<Jwt> issuerValidator =
                JwtValidators.createDefaultWithIssuer(properties.getIssuer());

        OAuth2TokenValidator<Jwt> audienceValidator = token -> {
            List<String> audience = token.getAudience();
            if (audience != null && audience.contains(properties.getAudience())) {
                return OAuth2TokenValidatorResult.success();
            }

            OAuth2Error error = new OAuth2Error(
                    OAuth2ErrorCodes.INVALID_TOKEN,
                    "Invalid audience",
                    null
            );
            return OAuth2TokenValidatorResult.failure(error);
        };

        delegate.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
                issuerValidator,
                audienceValidator
        ));

        return token -> delegate.decode(token)
                .flatMap(jwt -> blacklistReaderService.isBlacklisted(jwt.getId())
                        .flatMap(blacklisted -> {
                            if (blacklisted) {
                                return Mono.error(new BadJwtException("Token is blacklisted"));
                            }
                            return Mono.just(jwt);
                        }));
    }
}