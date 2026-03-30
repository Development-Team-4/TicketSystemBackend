package development.team.ticketsystem.authservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

    private String issuer;
    private String audience;
    private long accessTokenTtlSeconds;
    private long refreshTokenTtlSeconds;
    private String keyId;
}