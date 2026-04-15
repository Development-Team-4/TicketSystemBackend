package development.team.ticketsystem.notificationservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class BotClientConfiguration {
    @Bean
    public RestClient botRestClient(@Value("${services.bot.url}") String botServiceUrl) {
        return RestClient.builder()
                .baseUrl(botServiceUrl)
                .build();
    }
}