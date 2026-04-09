package development.team.ticketsystem.authservice.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import development.team.ticketsystem.authservice.service.PemKeyLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties({JwtProperties.class, JwtKeyProperties.class})
public class JwtConfig {

    private final JwtProperties jwtProperties;
    private final JwtKeyProperties jwtKeyProperties;
    private final PemKeyLoader pemKeyLoader;

    @Bean
    public RSAKey rsaJwk() {
        RSAPublicKey publicKey = pemKeyLoader.loadPublicKey(jwtKeyProperties.getPublicKey());
        RSAPrivateKey privateKey = pemKeyLoader.loadPrivateKey(jwtKeyProperties.getPrivateKey());

        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(jwtProperties.getKeyId())
                .algorithm(JWSAlgorithm.RS256)
                .keyUse(KeyUse.SIGNATURE)
                .build();
    }

    @Bean
    public JwtEncoder jwtEncoder(RSAKey rsaJwk) {
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(rsaJwk));
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JWKSet publicJwkSet(RSAKey rsaJwk) {
        return new JWKSet(rsaJwk.toPublicJWK());
    }

    @Bean
    public Map<String, Object> publicJwkResponse(JWKSet publicJwkSet) {
        return publicJwkSet.toJSONObject();
    }
}