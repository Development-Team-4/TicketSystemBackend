package development.team.ticketsystem.authservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.jwt.keys")
public class JwtKeyProperties {

    private Resource publicKey;
    private Resource privateKey;
}