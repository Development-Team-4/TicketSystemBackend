package development.team.ticketsystem.authservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.jwt.keys")
public class JwtKeyProperties {

    private String publicKeyPath;
    private String privateKeyPath;
}