package development.team.ticketsystem.gateway.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.security.jwt")
public class GatewayJwtProperties {

    private String issuer;
    private String audience;
    private String jwkSetUri;
}