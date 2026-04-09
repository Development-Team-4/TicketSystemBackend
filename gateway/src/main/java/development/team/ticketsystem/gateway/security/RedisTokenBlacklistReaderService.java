package development.team.ticketsystem.gateway.security;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RedisTokenBlacklistReaderService {

    private static final String PREFIX = "blacklist:jti:";

    private final ReactiveStringRedisTemplate redisTemplate;

    public Mono<Boolean> isBlacklisted(String jti) {
        if (jti == null || jti.isBlank()) {
            return Mono.just(false);
        }

        return redisTemplate.hasKey(PREFIX + jti)
                .map(Boolean.TRUE::equals);
    }
}