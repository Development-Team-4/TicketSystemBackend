package development.team.ticketsystem.gateway.config;

import development.team.ticketsystem.gateway.security.GatewayJwtAuthenticationConverter;
import development.team.ticketsystem.gateway.security.JsonAccessDeniedHandler;
import development.team.ticketsystem.gateway.security.JsonAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class GatewaySecurityConfig {

    private final ReactiveJwtDecoder jwtDecoder;
    private final GatewayJwtAuthenticationConverter jwtAuthenticationConverter;
    private final JsonAuthenticationEntryPoint authenticationEntryPoint;
    private final JsonAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/actuator/health", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .pathMatchers("/auth/**", "/.well-known/jwks.json").permitAll()
                        .pathMatchers("/users").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/tickets/**").hasAnyRole("USER", "ADMIN", "MODERATOR")
                        .pathMatchers("/tickets/**").hasAnyRole("ADMIN", "MODERATOR")
                        .anyExchange().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtDecoder(jwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter)
                        )
                )
                .build();
    }
}