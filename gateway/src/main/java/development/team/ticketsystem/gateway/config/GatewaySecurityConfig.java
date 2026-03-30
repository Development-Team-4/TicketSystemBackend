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

                        // public
                        .pathMatchers("/actuator/health", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .pathMatchers("/auth/**", "/.well-known/jwks.json").permitAll()

                        // users
                        .pathMatchers("/users").hasRole("ADMIN")
                        .pathMatchers("/users/**").authenticated()

                        // topics
                        .pathMatchers(HttpMethod.GET, "/topics")
                        .hasAnyRole("USER", "ADMIN", "SUPPORT")
                        .pathMatchers(HttpMethod.POST, "/topics")
                        .hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/topics/**")
                        .hasRole("ADMIN")

                        // categories read
                        .pathMatchers(HttpMethod.GET, "/topics/*/categories", "/categories/**")
                        .hasAnyRole("USER", "ADMIN", "SUPPORT")

                        // categories write
                        .pathMatchers(HttpMethod.POST, "/topics/*/categories")
                        .hasAnyRole("ADMIN", "SUPPORT")
                        .pathMatchers(HttpMethod.PATCH, "/categories/**")
                        .hasAnyRole("ADMIN", "SUPPORT")

                        // category staff management
                        .pathMatchers(HttpMethod.PUT, "/categories/*/staff")
                        .hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/categories/*/staff/**")
                        .hasRole("ADMIN")

                        // tickets read
                        .pathMatchers(HttpMethod.GET, "/tickets/**")
                        .hasAnyRole("USER", "ADMIN", "SUPPORT")

                        // tickets write
                        .pathMatchers("/tickets/**")
                        .hasAnyRole("ADMIN", "SUPPORT", "USER")

                        // tickets delete
                        .pathMatchers(HttpMethod.DELETE, "/tickets/**")
                        .hasAnyRole("ADMIN", "SUPPORT", "USER")

                        // notifications
                        .pathMatchers(HttpMethod.GET, "/notifications")
                        .hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/notifications/**")
                        .hasAnyRole("ADMIN","USER")
                        .pathMatchers(HttpMethod.POST, "/notifications/**")
                        .hasRole("ADMIN")

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