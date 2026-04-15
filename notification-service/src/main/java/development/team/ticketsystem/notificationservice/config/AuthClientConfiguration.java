package development.team.ticketsystem.notificationservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AuthClientConfiguration {

    @Bean
    public RestClient authRestClient(
            @Value("${services.auth.url}") String authServiceUrl
    ) {
        return RestClient.builder()
                .baseUrl(authServiceUrl)
                .build();
    }
}