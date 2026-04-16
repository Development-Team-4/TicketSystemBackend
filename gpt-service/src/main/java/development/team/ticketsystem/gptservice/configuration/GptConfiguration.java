package development.team.ticketsystem.gptservice.configuration;

import development.team.ticketsystem.gptservice.client.YaGPTClient;
import development.team.ticketsystem.gptservice.client.interfaces.LlmClient;
import development.team.ticketsystem.gptservice.service.YaGptChatService;
import development.team.ticketsystem.gptservice.service.interfaces.LlmServiceInterface;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GptConfiguration {
    @Bean
    public LlmServiceInterface llmServiceInterface() {
        return new YaGptChatService();
    }

    @Bean
    public LlmClient llmClient() {
        return new YaGPTClient();
    }
}
