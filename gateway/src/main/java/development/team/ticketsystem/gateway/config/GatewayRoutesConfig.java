package development.team.ticketsystem.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r
                        .path("/auth/**", "/users/**", "/.well-known/jwks.json")
                        .uri("http://localhost:8082"))
                .route("ticket-service", r -> r
                        .path("/tickets/**", "/topics/**", "/categories/**", "/debug/**")
                        .uri("http://localhost:8084"))
                .route("notification-service", r -> r
                        .path("/notifications/**")
                        .uri("http://localhost:8083"))
                .build();
    }
}