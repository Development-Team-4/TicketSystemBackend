package development.team.ticketsystem.gateway.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AuthHeadersPropagationFilter implements GlobalFilter, Ordered {

    private static final String HEADER_USER_ID = "X-User-Id";
    private static final String HEADER_USER_ROLES = "X-User-Roles";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {

        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> securityContext.getAuthentication())
                .cast(Authentication.class)
                .filter(Authentication::isAuthenticated)
                .flatMap(authentication -> {
                    if (!(authentication instanceof JwtAuthenticationToken jwtAuth)) {
                        return chain.filter(exchange);
                    }

                    String userId = resolveUserId(jwtAuth);
                    String rolesHeader = resolveRoles(jwtAuth);

                    ServerWebExchange mutatedExchange = exchange.mutate()
                            .request(builder -> {
                                builder.headers(headers -> {
                                    headers.remove(HEADER_USER_ID);
                                    headers.remove(HEADER_USER_ROLES);
                                });

                                if (userId != null && !userId.isBlank()) {
                                    builder.header(HEADER_USER_ID, userId);
                                }
                                if (rolesHeader != null && !rolesHeader.isBlank()) {
                                    builder.header(HEADER_USER_ROLES, rolesHeader);
                                }
                            })
                            .build();

                    return chain.filter(mutatedExchange);
                })
                .switchIfEmpty(chain.filter(exchange));
    }

    private String resolveUserId(JwtAuthenticationToken jwtAuth) {
        Object explicitUserId = jwtAuth.getToken().getClaims().get("userId");
        if (explicitUserId != null) {
            return explicitUserId.toString();
        }
        return jwtAuth.getToken().getSubject();
    }

    private String resolveRoles(JwtAuthenticationToken jwtAuth) {
        List<String> roles = jwtAuth.getToken().getClaimAsStringList("roles");
        if (roles == null || roles.isEmpty()) {
            return "";
        }
        return roles.stream().collect(Collectors.joining(","));
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}